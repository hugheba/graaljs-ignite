module.exports = {
    connection: {
        ipFinder: 'TcpDiscoveryMulticastIpFinder', // See https://apacheignite.readme.io/docs/tcpip-discovery
        addresses: ['127.0.0.1:47500..47509'],
        multicastGroup: '228.10.10.157', // Used only for discovery: 'TcpDiscoveryMulticastIpFinder'
    },
    caches: [
        {
            name: 'default',
            cacheMode: 'PARTITIONED' // Options are: LOCAL, PARTITIONED or REPLICATED, see https://apacheignite.readme.io/docs/cache-modes
        },
    ]
};