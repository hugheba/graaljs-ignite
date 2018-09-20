# hugheba-graaljs-ignite

Apache Ignite integration into Node.js running under GraalVM.

This module allows you to use an Apache Ignite in-memory cluster for caching, distributed messaging, grid computing and machine learning.

## Quickstart

Install the Node.js module

    $> npm install hugheba-graaljs-ignite
    
Run your Node.js application under GraalVM.

    $> npm \
          --jvm \
          --jvm.cp='./node_modules/hugheba-graaljs-ignite/lib/hugheba-graaljs-ignite.jar" \ 
          index.js
          
## Apache Ignite

> Apache Ignite is a memory-centric distributed database, caching, and processing platform
for transactional, analytical, and streaming workloads delivering in-memory speeds at petabyte scale

For more info, see: [https://ignite.apache.org](https://ignite.apache.org)

## GraalVM

> GraalVM is a universal virtual machine for running applications written in JavaScript, Python 3, Ruby, R, JVM-based languages like Java, Scala, Kotlin, and LLVM-based languages such as C and C++.
  
> GraalVM removes the isolation between programming languages and enables interoperability in a shared runtime. It can run either standalone or in the context of OpenJDK, Node.js, Oracle Database, or MySQL.

For more info, see: [https://www.graalvm.org/docs/](https://www.graalvm.org/docs/)

## Version Dependencies

| hugheba-graaljs-ignite Version | Ignite Version | GSON version | GroovyVersion |
|---|---|---|---|
| 1.0.0 | 2.6.0 | 2.8.5 | 2.5.2 |

## Example

```javascript
var IgniteBridge = require('hugheba-graaljs-ignite');

var config = {
        connection: {
            // @see https://apacheignite.readme.io/docs/tcpip-discovery
            ipFinder: 'TcpDiscoveryMulticastIpFinder', 
            // IP Address of other nodes in cluster
            addresses: ['127.0.0.1:47500..47509'],
            // Used only for discovery: 'TcpDiscoveryMulticastIpFinder'
            multicastGroup: '228.10.10.157', 
        },
        caches: [
            {
                // Name for cache in Ignite
                name: 'default',
                // Options are: LOCAL, PARTITIONED or REPLICATED, 
                // @see https://apacheignite.readme.io/docs/cache-modes
                cacheMode: 'PARTITIONED' 
            },
        ],
};

var ib = new IgniteBridge(config),
    ignite = ib.getIgnite(),
    defaultCache = ib.getCaches()[config.caches[0].name],
    cacheKey = 'cache_key_1',
    topic = 'my_cluster_topic';

// Cache Example
defaultCache.put(cacheKey, 'My Cache Data');
console.log('Cache returned ' + defaultCache.get(cacheKey));

// Messaging Example
ib.subscribe(topic, function(message) {
    console.log('Topic ' + topic + ' received message: ' + message);
});
ib.broadcast(topic, {
    status: true,
    code: 200,
    message: 'This object will be JSON serialized/deserialized over the wire, and sent to every subscriber in the cluster.',
});

```