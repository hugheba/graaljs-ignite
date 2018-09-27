package com.hugheba.graal.js.ignite.model

import org.apache.ignite.IgniteAtomicLong

class IgniteBridgeConnectionConfig {
    IgniteBridgeTcpDiscoveryIpFinder ipFinder = IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryMulticastIpFinder
    List<String> addresses
    String multicastGroup
    String awsBucket
    String googleProject
    String googleBucket
    String kubeNamespace
    String kubeServiceName
}

enum IgniteBridgeTcpDiscoveryIpFinder {
    TcpDiscoveryMulticastIpFinder,
    TcpDiscoveryVmIpFinder,
    TcpDiscoveryKubernetesIpFinder,
    TcpDiscoveryS3IpFinder,
    TcpDiscoveryGoogleStorageIpFinder
}

