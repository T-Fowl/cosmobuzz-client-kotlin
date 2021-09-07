package com.tfowl.cosmobuzz.fakes

import com.tfowl.cosmobuzz.CosmoSocket
import com.tfowl.cosmobuzz.IncomingEvent
import com.tfowl.cosmobuzz.OutgoingEvent
import java.util.concurrent.atomic.AtomicBoolean

class FakeCosmoSocket : CosmoSocket {

    val listeners = mutableListOf<(IncomingEvent) -> Unit>()
    val sent = mutableListOf<OutgoingEvent>()
    val connected = AtomicBoolean(true)

    override fun receive(callback: (IncomingEvent) -> Unit) {
        listeners += callback
    }

    override suspend fun send(event: OutgoingEvent) {
        sent += event
    }

    fun emit(event: IncomingEvent) {
        listeners.forEach { it(event) }
    }

    override fun disconnect() {
        connected.set(false)
    }
}