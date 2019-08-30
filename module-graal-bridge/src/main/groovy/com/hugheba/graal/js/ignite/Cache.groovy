package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache

class Cache {

    Ignite ignite
    String cacheName
    IgniteCache<Object, Object> cache

    Cache(Ignite ignite, String cacheName) {
        this.ignite = ignite
        this.cacheName = cacheName
        cache = ignite.getOrCreateCache(cacheName)
    }

    void put(Object key, Object value) {
        cache.put(key, value)
    }

    Object get(Object key) {
        cache.get(key)
    }

    boolean containsKey(Object key) {
        cache.containsKey(key)
    }

    Map<Object, Object> getAll(Set<Object> keys) {
        cache.getAll(keys)
    }
}
