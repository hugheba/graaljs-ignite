package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.cache.query.ScanQuery

class Cache {

    Ignite ignite
    String cacheName
    IgniteCache<String, Object> cache

    Cache(Object ignite, String cacheName) {
        this.ignite = ignite
        this.cacheName = cacheName
        cache = ignite.getOrCreateCache(cacheName)
    }

    void put(Object key, Object value) {
        cache.put(key.toString(), value)
    }

    Object get(Object key) {
        cache.get(key.toString())
    }

    boolean containsKey(Object key) {
        cache.containsKey(key.toString())
    }

    Map<Object, Object> getAll(Set<Object> keys) {

        cache.getAll(keys.collect { it.toString() }.toSet())
    }

    boolean replace(Object key, Object value) {
        cache.replace(key.toString(), value)
    }

    void clear(Object key) {
        cache.clear(key.toString())
    }

    List<String> getCacheKeys() {
        List<String> keys = new ArrayList<>();
        cache.query(new ScanQuery<>(null)).forEach {entry -> keys.add((String) entry.getKey())}

        keys
    }

}
