package com.hugheba.graal.js.ignite.model

import org.graalvm.polyglot.Value

class IgniteBridgeConfiguration {
    String gridName
    String instanceName
    IgniteBridgeConnectionConfig connection
    Map<String, IgniteBridgeCacheConfig> caches
    Value beforeIgniteStart
    Value afterIgniteStart
    Value beforeIgniteStop
    Value afterIgniteStop
}
