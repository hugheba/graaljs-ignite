package com.hugheba.graal.js.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteMessaging
import org.apache.ignite.lang.IgniteBiPredicate
import org.graalvm.polyglot.Value

class EventBus {

    class EBEventListener implements IgniteBiPredicate<UUID, String> {

        Value jsEventListener

        EBEventListener(Value jsEventListener) {
            setJsEventListener(jsEventListener)
        }

        @Override
        boolean apply(UUID uuid, String msg) {
            if (jsEventListener!=null) (jsEventListener).executeVoid(msg)

            true
        }
    }

    final Ignite ignite
    final IgniteMessaging igniteMessaging
    final Map<String, EBEventListener> listeners = [:]

    EventBus(Ignite ignite) {
        this.ignite = ignite
        igniteMessaging = ignite.message()
    }

    void subscribe(String topic, Value jsEventListener) {
        listeners[topic] = new EBEventListener(jsEventListener)
        igniteMessaging.localListen(topic, listeners[topic])
    }

    void unsubscribe(String topic, Value jsEventListener) {
        igniteMessaging.stopLocalListen(topic, listeners[topic])
    }

    void broadcast(String topic, String message) {
        igniteMessaging.sendOrdered(topic, message, 1000)
    }
}
