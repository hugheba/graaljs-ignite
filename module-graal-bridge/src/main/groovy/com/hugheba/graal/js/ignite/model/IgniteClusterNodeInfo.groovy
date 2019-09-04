package com.hugheba.graal.js.ignite.model

class IgniteClusterNodeInfo {
    String id
    Boolean local
    Boolean client
    List<String> hostnames
    List<String> addresses
    Map<String,String> attributes
}
