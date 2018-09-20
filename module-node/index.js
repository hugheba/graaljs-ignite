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
            var configCaches = this.config.caches;
            configCaches.forEach( function(cache) {
                if (cache.name) {
                    me.caches[cache.name] = me.javaBridge.getOrCreateCache(cache.name);
                }
            });
        } catch(e) {
            console.error('Unable to load Ignite caches!', e);
        }
    }

    getIgnite() {
        return this.javaBridge.ignite;
    }

    getCaches() {
        return this.caches;
    }

    subscribe(topic, callback) {
        this.javaBridge.subscribe(topic, callback);
    }

    unsubscribe(topic, callback) {
        this.javaBridge.unsubscribe(topic, callback);
    }

    broadcast(topic, message) {
        this.javaBridge.broadcast(topic, JSON.stringify(message));
    }
}