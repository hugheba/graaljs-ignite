package com.hugheba.graal.js.ignite.model

class IgniteBridgeConnectionConfig {
    IgniteBridgeTcpDiscoveryIpFinder ipFinder
    List<String> addresses
    String multicastGroup
}

enum IgniteBridgeTcpDiscoveryIpFinder {
    TcpDiscoveryMulticastIpFinder, TcpDiscoveryVmIpFinder
}

