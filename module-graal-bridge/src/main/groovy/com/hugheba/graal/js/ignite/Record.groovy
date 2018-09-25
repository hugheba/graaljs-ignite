package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.cache.query.ContinuousQuery
import org.apache.ignite.cache.query.QueryCursor
import org.apache.ignite.cache.query.ScanQuery
import org.graalvm.polyglot.Value

import javax.cache.Cache

class Record {

    final Ignite ignite
    final String recordName
    final IgniteCache<String, Object> cache
    final Map<String, Set<Closure>> pl = [:]
    ContinuousQuery<String, Object> continuousQuery = null
    QueryCursor<Cache.Entry<String, Object>> queryCursor = null

    Record(Ignite ignite, String recordName) {
        this.ignite = ignite
        this.recordName = recordName
        cache = this.ignite.getOrCreateCache("record_${recordName}") as IgniteCache<String, Object>
    }

    private attachContinuousQuery() {
        def pl = this.pl
        try {
            Closure processPropertyEvent = { event ->
                String key = event?.key as String
                if (key && pl?.keySet()?.contains(key)) {
                    pl[key]?.each { it.call(event.value) }
                }
            }

            continuousQuery = new ContinuousQuery<>()
            continuousQuery.setInitialQuery(new ScanQuery<String, Object>({ k, v -> pl.keySet().contains(k) }))
            continuousQuery.setLocalListener({ events -> events.each { event -> processPropertyEvent(event) } })
//            continuousQuery.remoteFilterFactory = new Factory<CacheEntryEventFilter<String, Object>>() {
//                @Override
//                CacheEntryEventFilter<String, Object> create() {
//                    new CacheEntryEventFilter<String, Object>() {
//                        @Override
//                        boolean evaluate(CacheEntryEvent<? extends String, ?> event) throws CacheEntryListenerException {
//                            pl.keySet().contains(event.key)
//                        }
//                    }
//                }
//            }

            queryCursor = cache.query(continuousQuery)
            queryCursor.each { processPropertyEvent(it) }
        } catch(Exception e) {
            println e.message
        }
    }

    Object get(String propertyName) {
        cache.get(propertyName)
    }

    void put(String propertyName, Object value) {
        cache.put(propertyName, value)
    }

    void subscribe(String propertyName, Value jsRecordListener) {
        subscribe(propertyName, {val -> jsRecordListener.executeVoid(val)})
    }

    void subscribe(String propertyName, Closure wrapperListener) {
        if (!continuousQuery) attachContinuousQuery()
        if (!pl[propertyName]) pl.put(propertyName, [] as Set<Closure>)
        pl[propertyName].add(wrapperListener)
    }


}
