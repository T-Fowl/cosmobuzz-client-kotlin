package com.tfowl.cosmobuzz.fakes

import com.tfowl.cosmobuzz.AbstractCosmoSocket
import com.tfowl.cosmobuzz.IncomingEvent
import com.tfowl.cosmobuzz.OutgoingEvent
import com.tfowl.cosmobuzz.Request
import java.util.concurrent.atomic.AtomicBoolean

class FakeCosmoSocket(
    val reqHandler: (Request<out Any>) -> Any =
        { throw Throwable("Request not supported: $it") }
) :
    AbstractCosmoSocket() {

    val sent = mutableListOf<OutgoingEvent>()
    val connected = AtomicBoolean(true)

    override suspend fun send(event: OutgoingEvent) {
        sent += event
    }

    public override fun emit(event: IncomingEvent) = super.emit(event)

    override suspend fun <T : Any> request(req: Request<T>): T {
        return reqHandler(req) as T
    }

    override fun disconnect() {
        connected.set(false)
    }
}