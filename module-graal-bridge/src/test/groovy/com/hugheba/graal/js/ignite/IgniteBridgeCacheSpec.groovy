package com.hugheba.graal.js.ignite

import com.hugheba.graal.js.ignite.model.IgniteBridgeCacheConfig
import com.hugheba.graal.js.ignite.model.IgniteBridgeConfiguration
import com.hugheba.graal.js.ignite.model.IgniteBridgeConnectionConfig

import spock.lang.Shared
import spock.lang.Specification


class IgniteBridgeCacheSpec extends Specification {

    static final String RECORD_NAME = 'record1'
    static final String PROP_FIRSTNAME = 'firstname'

    @Shared IgniteBridge igniteBridge

    def setupSpec() {
        igniteBridge  = new IgniteBridge(new IgniteBridgeConfiguration(
                connection: new IgniteBridgeConnectionConfig(
                        addresses: ["127.0.0.1:47500..47509"],
                        multicastGroup: "228.10.10.157"
                ),
                caches: [
                        new IgniteBridgeCacheConfig(name: 'default')
                ]
        ))
    }

    def cleanupSpec() {
        igniteBridge?.ignite?.close()
    }

    def "should create cache"() {
        when:
        def cache = igniteBridge.getCache('default')

        then:
        cache != null
        cache instanceof Cache
    }

    def "can save property to Record"() {
        when:
        String bob = 'Bob'
        Record record1 = igniteBridge.getRecord(RECORD_NAME)
        record1.put(PROP_FIRSTNAME, bob)

        then:
        bob == record1.get(PROP_FIRSTNAME)
    }

    def "can subscribe to Record"() {
        when:
        Object result = null
        String sam = 'Sam'
        Record record1 = igniteBridge.getRecord(RECORD_NAME)
        record1.subscribe(PROP_FIRSTNAME, {x ->
            result = x
        })
        record1.put(PROP_FIRSTNAME, sam)
        Thread.sleep(5000)

        then:
        result == sam
    }

}
