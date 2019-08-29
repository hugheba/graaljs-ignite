package com.hugheba.graal.js.ignite

import org.apache.ignite.IgniteException
import org.apache.ignite.lifecycle.LifecycleBean
import org.apache.ignite.lifecycle.LifecycleEventType
import org.graalvm.polyglot.Value

class Lifecycle implements LifecycleBean {

    Value beforeIgniteStart, afterIgniteStart, beforeIgniteStop, afterIgniteStop

    @Override
    void onLifecycleEvent(LifecycleEventType evt) throws IgniteException {
        switch(evt) {
            case LifecycleEventType.BEFORE_NODE_START:
                beforeNodeStartCallback();
                break;
            case LifecycleEventType.AFTER_NODE_START:
                afterNodeStartCallback()
                break;
            case LifecycleEventType.BEFORE_NODE_STOP:
                beforeNodeStopCallback()
                break;
            case LifecycleEventType.AFTER_NODE_STOP:
                afterNodeStopCallback()
                break;
        }
    }

    void beforeNodeStartCallback() {
        try {
            if (beforeIgniteStart?.canExecute()) {
                beforeIgniteStart.executeVoid()
            }
        } catch(Exception e) { }
    }

    void afterNodeStartCallback() {
        try {
            if (afterIgniteStart?.canExecute()) {
                afterIgniteStart.executeVoid()
            }
        } catch(Exception e) { }
    }

    void beforeNodeStopCallback() {
        try {
            if (beforeIgniteStop?.canExecute()) {
                beforeIgniteStop.executeVoid()
            }
        } catch(Exception e) { }
    }

    void afterNodeStopCallback() {
        try {
            if (afterIgniteStop?.canExecute()) {
                afterIgniteStop.executeVoid()
            }
        } catch(Exception e) { }
    }

}
