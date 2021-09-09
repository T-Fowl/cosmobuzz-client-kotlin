package com.tfowl.cosmobuzz

sealed class IncomingEvent {
    data class PlayersChanged(val players: List<Player>) : IncomingEvent()
    data class RoomSettingsChanged(val settings: RoomSettings) : IncomingEvent()
    data class PlayerBuzzed(val id: String) : IncomingEvent()
}

sealed class OutgoingEvent {
    data class UpdateRoomSettings(val settings: RoomSettings) : OutgoingEvent()
    object ResetBuzzers : OutgoingEvent()
}

sealed class Request<T : Any> {
    object CreateRoom : Request<String>()
}

interface CosmoSocket {

    fun receive(callback: (IncomingEvent) -> Unit)

    suspend fun send(event: OutgoingEvent)

    suspend fun <T : Any> request(req: Request<T>): T

    fun disconnect()
}

abstract class AbstractCosmoSocket : CosmoSocket {
    private val listeners = mutableListOf<(IncomingEvent) -> Unit>()

    override fun receive(callback: (IncomingEvent) -> Unit) {
        listeners += callback
    }

    protected open fun emit(event: IncomingEvent) {
        listeners.forEach { it(event) }
    }
}

interface CosmoSocketFactory {
    suspend fun create(): CosmoSocket
}