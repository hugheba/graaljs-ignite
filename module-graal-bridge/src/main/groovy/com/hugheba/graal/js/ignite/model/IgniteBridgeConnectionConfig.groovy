package com.hugheba.graal.js.ignite.model

import org.apache.ignite.IgniteAtomicLong

class IgniteBridgeConnectionConfig {
    Integer discoveryLocalPort
    Integer discoveryLocalPortRange
    IgniteBridgeTcpDiscoveryIpFinder ipFinder = IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryMulticastIpFinder
    List<String> addresses
    String multicastGroup
    String awsBucket
    String googleProject
    String googleBucket
    String kubeNamespace
    String kubeServiceName
    Integer communicationLocalPort
    Integer communicationLocalPortRange
}

enum IgniteBridgeTcpDiscoveryIpFinder {
    TcpDiscoveryMulticastIpFinder,
    TcpDiscoveryVmIpFinder,
    TcpDiscoveryKubernetesIpFinder,
    TcpDiscoveryS3IpFinder,
    TcpDiscoveryGoogleStorageIpFinder
}

