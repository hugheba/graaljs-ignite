package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache

class Cache {

    Ignite ignite
    String cacheName
    IgniteCache<String, Object> cache

    Cache(Ignite ignite, String cacheName) {
        this.ignite = ignite
        this.cacheName = cacheName
        cache = ignite.getOrCreateCache(cacheName)
    }

    void put(String key, Object value) {
        cache.put(key, value)
    }

    Object get(String key) {
        cache.get(key)
    }
}
