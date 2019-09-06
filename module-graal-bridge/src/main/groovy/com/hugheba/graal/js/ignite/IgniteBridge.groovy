package com.hugheba.graal.js.ignite


import com.amazonaws.auth.EnvironmentVariableCredentialsProvider
import com.google.gson.Gson
import com.hugheba.graal.js.ignite.exception.IgniteBridgeConfigurationException
import com.hugheba.graal.js.ignite.model.IgniteBridgeCacheConfig
import com.hugheba.graal.js.ignite.model.IgniteBridgeConfiguration
import com.hugheba.graal.js.ignite.model.IgniteBridgeConnectionConfig
import com.hugheba.graal.js.ignite.model.IgniteBridgeTcpDiscoveryIpFinder
import com.hugheba.graal.js.ignite.model.IgniteClusterNodeInfo
import com.hugheba.graal.js.ignite.model.IgniteInfo
import groovy.transform.CompileStatic
import groovy.util.logging.Log
import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cluster.ClusterNode
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.lifecycle.LifecycleBean
import org.apache.ignite.spi.communication.CommunicationSpi
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi
import org.apache.ignite.spi.discovery.DiscoverySpi
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.gce.TcpDiscoveryGoogleStorageIpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.kubernetes.TcpDiscoveryKubernetesIpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder
import org.graalvm.polyglot.Value

@Log
@CompileStatic
class IgniteBridge {

    final EventBus eventBus
    final Map<String, Cache> caches = [:]
    final Map<String, Record> records = [:]
    final Map<String, Counter> counters = [:]
    final Lifecycle lifecycle = new Lifecycle()
    static final Gson gson = new Gson()

    IgniteBridgeConfiguration config
    Ignite ignite


    static isRunning(String instanceName) {
        if (Ignition.state(instanceName)) {
            return true
        } else {
            false
        }
    }

    IgniteBridge(Value config) {
        this(gson.fromJson(config.as(String) as String, IgniteBridgeConfiguration) as IgniteBridgeConfiguration)
    }

    IgniteBridge(IgniteBridgeConfiguration config) {

        if (ignite) {
            println("Ignite already started, reusing instance.")
            return
        }

        println("Starting Ingite with configuration: ${gson.toJson(config)}")

        this.config = config

        lifecycle.with {
            beforeIgniteStart = config.beforeIgniteStart
            afterIgniteStart = config.afterIgniteStart
            beforeIgniteStop = config.beforeIgniteStop
            afterIgniteStop = config.afterIgniteStop
        }

        def conn = config.connection;

        DiscoverySpi discoverySpi = new TcpDiscoverySpi(ipFinder: buildIpFinder())
        if (conn.discoveryLocalPort) discoverySpi.localPort = conn.discoveryLocalPort
        if (conn.discoveryLocalPortRange) discoverySpi.localPortRange = conn.discoveryLocalPortRange

        CommunicationSpi communicationSpi = new TcpCommunicationSpi()
        if (conn.communicationLocalPort) communicationSpi.localPort = conn.communicationLocalPort

        IgniteConfiguration cfg = new IgniteConfiguration(
                gridName: config.gridName,
                igniteInstanceName: config.instanceName,
                lifecycleBeans: [lifecycle] as LifecycleBean[],
                discoverySpi: discoverySpi,
                communicationSpi: communicationSpi,
                cacheCfg: buildCaches(),
        )

        ignite = Ignition.start(cfg)
        eventBus = new EventBus(ignite)
    }

    void shutdown() {
        Ignition.stop(false);
    }

    private TcpDiscoveryIpFinder buildIpFinder() {
        TcpDiscoveryIpFinder ipFinder
        IgniteBridgeConnectionConfig connCfg = this.config.connection
        switch(connCfg.ipFinder) {
            case {it == IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryS3IpFinder}:
                if (!connCfg.awsBucket) {
                    throw new IgniteBridgeConfigurationException(
                            "Missing [config.connection.awsBucket] for ${IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryS3IpFinder}"
                    )
                }
                ipFinder = new TcpDiscoveryS3IpFinder(
                        bucketName: connCfg.awsBucket,
                        awsCredentials: new EnvironmentVariableCredentialsProvider().getCredentials()
                )
                break
            case {it == IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryGoogleStorageIpFinder}:
                if (!connCfg.googleProject || !connCfg.googleBucket) {
                    throw new IgniteBridgeConfigurationException(
                            "Missing [config.connection.googleProject,config.connetion.googleBucket] for ${IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryGoogleStorageIpFinder}"
                    )
                }
                ipFinder = new TcpDiscoveryGoogleStorageIpFinder(
                        projectName: connCfg.googleProject,
                        bucketName: connCfg.googleProject,
                )
                break
            case {it == IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryKubernetesIpFinder}:
                if (!connCfg.kubeNamespace || !connCfg.kubeServiceName) {
                    throw new IgniteBridgeConfigurationException(
                            "Missing [config.connection.kubeNamespace,config.connection.kubeServiceName] for ${IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryKubernetesIpFinder}"
                    )
                }
                ipFinder = new TcpDiscoveryKubernetesIpFinder(
                        namespace: connCfg.kubeNamespace,
                        serviceName: connCfg.kubeServiceName
                )
                break
            case {it == IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryVmIpFinder}:
                if (!connCfg.addresses) {
                    throw new IgniteBridgeConfigurationException(
                            "Missing [config.connection.addresses] for ${IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryVmIpFinder.name()}"
                    )
                }
                ipFinder = new TcpDiscoveryVmIpFinder(addresses: connCfg.addresses)
                break
//            case {it == IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryMulticastIpFinder}:
            default:
                if (!connCfg.addresses || !connCfg.multicastGroup) {
                    throw new IgniteBridgeConfigurationException(
                            "Missing [config.connection.addresses or config.connection.multicastGroup] for ${IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryMulticastIpFinder.name()}"
                    )
                }
                ipFinder = new TcpDiscoveryMulticastIpFinder()
                if (connCfg.multicastGroup) ipFinder.multicastGroup = connCfg.multicastGroup
                if (connCfg.addresses) ipFinder.addresses = connCfg.addresses
                break
        }

        ipFinder
    }

    private CacheConfiguration[] buildCaches() {
        this.config.caches.keySet().collect {
            IgniteBridgeCacheConfig cacheConfig = this.config.caches[it]
            def cache = new CacheConfiguration<String, String>(name: it, cacheMode: cacheConfig.cacheMode as CacheMode)
        } as CacheConfiguration[]
    }

    Ignite getIgnite() {
        ignite
    }

    EventBus getEventBus() {
        eventBus
    }

    String getInfo() {
        IgniteInfo info = new IgniteInfo()
        info.clusterSize = ignite.cluster().nodes().size()
        info.clusterNodes = ignite.cluster().nodes().collect { ClusterNode node ->
            new IgniteClusterNodeInfo(
                    id: node.id().toString(),
                    local: node.local,
                    hostnames: node.hostNames().collect {it.toString()},
                    addresses: node.addresses().collect {it.toString()},
//                    attributes: (node.attributes().collectEntries { key,value ->
//                        ["${key}".toString(): "${value.toString()}".toString()]
//                    }),
                    client: node.attributes().get('org.apache.ignite.cache.client') as Boolean
            )
        }

        gson.toJson(info)
    }

    Cache getCache(String cacheName) {
        Cache cache = caches.get(cacheName)
        if (!cache) {
            cache = new Cache(ignite, cacheName)
            caches.put(cacheName, cache)
        }

        cache
    }

    Record getRecord(String recordName) {
        Record record = records.get(recordName)
        if (!record) {
            record = new Record(ignite, recordName)
            records.put(recordName, record)
        }

        record
    }

    Counter getCounter(String counterName, Long initialValue = 0) {
        Counter counter = counters.get(counterName)
        if (!counter) {
            counter = new Counter(ignite, counterName, initialValue)
            counters.put(counterName, counter)
        }

        counter
    }


}
