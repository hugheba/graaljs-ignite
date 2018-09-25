module.exports = class IgniteBridge {

    constructor(config) {
        this.config = config;
        this.caches = {};
        this.connect();
        this.initCaches();
    }

    connect() {
        const IB = Java.type('com.hugheba.graal.js.ignite.IgniteBridge');
        this.javaBridge = new IB(JSON.stringify(this.config));
    }

    initCaches() {
        var me = this;
        try {
            let configCaches = this.config.caches;
            configCaches.forEach( function(cache) {
                if (cache.name) {
                    me.caches[cache.name] = me.javaBridge.getCache(cache.name);
                }
            });
        } catch(e) {
            console.error('Unable to load Ignite caches!', e);
        }
    }

    getIgnite() {
        return this.javaBridge.ignite;
    }

    getCache(cacheName) {
        return this.javaBridge.getCache(cacheName)
    }

    getCounter(counterName, initialValue = 0) {
        return this.javaBridge.getCounter(counterName, initialValue)
    }

    getRecord(recordName) {
        return this.javaBridge.getRecord(recordName);
    }

    subscribe(topic, callback) {
        this.javaBridge.subscribe(topic, callback);
    }

    unsubscribe(topic, callback) {
        this.javaBridge.unsubscribe(topic, callback);
    }

    broadcast(topic, message) {
        let encodedMessage = message;
        if (typeof message !== 'string') {
            encodedMessage = JSON.stringify(message);
        }
        this.javaBridge.broadcast(topic, encodedMessage);
    }

};