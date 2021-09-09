package com.tfowl.cosmobuzz.fakes

import com.tfowl.cosmobuzz.AbstractCosmoSocket
import com.tfowl.cosmobuzz.IncomingEvent
import com.tfowl.cosmobuzz.OutgoingEvent
import java.util.concurrent.atomic.AtomicBoolean

class FakeCosmoSocket : AbstractCosmoSocket() {
    
    val sent = mutableListOf<OutgoingEvent>()
    val connected = AtomicBoolean(true)

    override suspend fun send(event: OutgoingEvent) {
        sent += event
    }

    public override fun emit(event: IncomingEvent) = super.emit(event)

    override fun disconnect() {
        connected.set(false)
    }
}