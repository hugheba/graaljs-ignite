package com.hugheba.graal.js.ignite.model

import org.apache.ignite.IgniteAtomicLong

class IgniteBridgeConnectionConfig {
    IgniteBridgeTcpDiscoveryIpFinder ipFinder = IgniteBridgeTcpDiscoveryIpFinder.TcpDiscoveryMulticastIpFinder
    List<String> addresses
    String multicastGroup
}

enum IgniteBridgeTcpDiscoveryIpFinder {
    TcpDiscoveryMulticastIpFinder, TcpDiscoveryVmIpFinder
}

