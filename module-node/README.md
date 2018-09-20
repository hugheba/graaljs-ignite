# hugheba-graaljs-ignite

Apache Ignite integration into Node.js running under GraalVM.

This module allows you to use an Apache Ignite in-memory cluster for caching, distributed messaging, grid computing and machine learning.

## Quickstart

Install the Node.js module

    $> npm install hugheba-graaljs-ignite
    
Run your Node.js application under GraalVM.

    $> node \
          --jvm \
          --jvm.cp='./node_modules/hugheba-graaljs-ignite/lib/hugheba-graaljs-ignite-all.jar" \ 
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

The IgniteBridge requires several Java dependencies.

This bundle provides two options for adding dependencies through the node --jvm.cp argument.

- _**hugheba-graaljs-ignite-all.jar**_ : An all-in-one jar with with the dependencies below included.
- _**hugheba-graaljs-ignite.jar**_ : A jar containing only module functionality and no dependences.

### Manual Dependency Management 

To use this plugin and supply your own dependencies, download the required jars and add them to a folder named `lib` the root of your project.
 
Modify the run command above to look like this:

    $> node \
          --jvm \
          --jvm.cp="lib/*:./node_modules/hugheba-graaljs-ignite/lib/hugheba-graaljs-ignite.jar" \
          index.js

| hugheba-graaljs-ignite Version | Ignite Version | GSON Version | Groovy Version |
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
    console.log('Topic ' + topic + '.message: ' + JSON.parse(message).message);
});
ib.broadcast(topic, {
    status: true,
    code: 200,
    message: 'This object will be JSON serialized over the wire, and sent to every subscriber in the cluster.',
});

```