package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteAtomicLong

class Counter {

    private final Ignite ignite
    private final String counterName
    IgniteAtomicLong counter

    Counter(Ignite ignite, String counterName, Long initialValue = 0) {
        this.ignite = ignite
        this.counterName = counterName
        counter = this.ignite.atomicLong(this.counterName, initialValue, true)
    }

    Long get() {
        counter.get()
    }

    Long incrementAndGet() {
        counter.incrementAndGet()
    }

    Long decrementAndGet() {
        counter.decrementAndGet()
    }


}
