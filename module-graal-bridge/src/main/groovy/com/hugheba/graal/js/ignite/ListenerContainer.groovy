package com.hugheba.graal.js.ignite

import org.graalvm.polyglot.Value

class ListenerContainer {
    IgniteBridge.EBEventListener ebEventListener
    Value jsEventListener
}
