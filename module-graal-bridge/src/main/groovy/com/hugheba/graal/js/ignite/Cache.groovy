package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.cache.query.ScanQuery

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

    boolean replace(Object key, Object value) {
        cache.replace(key, value)
    }

    void clear(Object key) {
        cache.clear(key)
    }

    List<Object> getCacheKeys() {
        List<Object> keys = new ArrayList<>();
        cache.query(new ScanQuery<>(null)).forEach {entry -> keys.add((Object) entry.getKey())}

        keys
    }

}
