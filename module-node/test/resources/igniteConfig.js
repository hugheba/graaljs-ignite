module.exports = {
    connection: {
        /**
         * Select node discovery method and options below
         *
         * @see https://apacheignite.readme.io/docs/tcpip-discovery
         */
        ipFinder: 'TcpDiscoveryMulticastIpFinder',

        /**
         * ipFinder: [TcpDiscoveryVmIpFinder or TcpDiscoveryMulticastIpFinder]
         */
        addresses: ['127.0.0.1:47500..47509'],

        /**
         * ipFinder: TcpDiscoveryMulticastIpFinder
         */
        multicastGroup: '228.10.10.157',

        /**
         * ipFinder: TcpDiscoveryS3IpFinder
         *
         * Use AWS S3 for node discovery.
         *
         * AWS credentials must be provided in the environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY.
         *
         * @see https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
         * @see https://apacheignite-mix.readme.io/docs/google-compute-engine
         */
        awsBucket: 'project-ingite-ipfinder',

        /**
         * ipFinder: TcpDiscoveryGoogleStorageIpFinder
         *
         * Use Google Cloud for node discovery.
         *
         * Google Cloud credentials must be available at a path specified by the environment variable
         * GOOGLE_APPLICATION_CREDENTIALS.
         *
         * @see https://cloud.google.com/docs/authentication/production
         */
        googleProject: 'my-google-project',
        googleBucket: 'project-ignite-ipfinder',

        /**
         * ipFinder: TcpDiscoveryKubernetesIpFinder
         *
         * Use Kubernetes for node discovery.
         *
         * @see https://apacheignite-mix.readme.io/v2/docs/kubernetes-discovery
         */
        kubeNamespace: 'default',
        kubeServiceName: 'ignite'
    },

    /**
     * Preconfigure caches with explicit options.
     *
     * This is optional, caches can be created on the fly at runtime.
     */
    caches: {
        default : {
            /**
             * CacheMode
             *
             * @see https://apacheignite.readme.io/docs/cache-modes
             * @param {string} cacheMode - One of LOCAL, PARTITIONED or REPLICATED
             */
            cacheMode: 'PARTITIONED'
        },
    }
};