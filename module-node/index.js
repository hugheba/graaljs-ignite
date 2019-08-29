const uuidv1 = require('uuid/v1');

module.exports = class IgniteBridge {

    constructor(config) {
            this.config = config;
            this.caches = {};
            this.connect();
            this.initCaches();
    }

    connect() {
        const IB = Java.type('com.hugheba.graal.js.ignite.IgniteBridge');
        /*if (this.config.instanceName this.config.instanceName = uuidv1();*/
        this.javaBridge = new IB(JSON.stringify(this.config));
    }

    shutdown() {
        this.javaBridge.shutdown();
    }

    initCaches() {
        var me = this;
        try {
            for (var cacheName in this.config.caches) {
                    me.caches[cacheName] = me.javaBridge.getCache(cacheName);
            }
        } catch(e) {
            console.error('Unable to load Ignite caches!', e);
        }
    }

    getIgnite() {
        return this.javaBridge.ignite;
    }

    getEventBus() {
        return this.javaBridge.getEventBus();
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