package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.cache.query.ScanQuery

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

    boolean containsKey(String key) {
        cache.containsKey(key)
    }

    Map<String, Object> getAll(Set<String> keys) {
        cache.getAll(keys)
    }

    boolean replace(String key, Object value) {
        cache.replace(key, value)
    }

    void clear(String key) {
        cache.clear(key)
    }

    List<String> getCacheKeys() {
        List<String> keys = new ArrayList<>();
        cache.query(new ScanQuery<>(null)).forEach {entry -> keys.add((String) entry.getKey())}

        keys
    }

}
