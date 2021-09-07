package com.tfowl.cosmobuzz

import com.tfowl.socketio.emitAwait
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class SocketIOCosmoSocket(private val socket: Socket) : CosmoSocket {

    private val listeners = mutableListOf<(IncomingEvent) -> Unit>()

    init {
        socket.on(EVENT_UPDATE_SETTINGS) { args ->
            val settings = (args.first() as JSONObject).deserializeRoomSettings()
            settings?.let { emit(IncomingEvent.RoomSettingsChanged(settings)) }
        }
        socket.on(EVENT_PLAYERS_CHANGED) { args ->
            val array = (args.first() as JSONArray)
            val players = List(array.length()) { array.getJSONObject(it) }.mapNotNull { it.deserializePlayer() }
            emit(IncomingEvent.PlayersChanged(players))
        }
        socket.on(EVENT_PLAYER_BUZZER) { args ->
            val id = args.first().toString()
            emit(IncomingEvent.PlayerBuzzed(id))
        }
    }

    private fun emit(event: IncomingEvent) {
        listeners.forEach { it(event) }
    }

    override fun receive(callback: (IncomingEvent) -> Unit) {
        listeners += callback
    }

    override suspend fun send(event: OutgoingEvent) {
        when (event) {
            is OutgoingEvent.UpdateRoomSettings -> socket.emitAwait(
                EVENT_UPDATE_SETTINGS,
                event.settings.toJsonObject()
            )
            is OutgoingEvent.ResetBuzzers       -> socket.emitAwait(EVENT_RESET_BUZZERS)
        }
    }

    override fun disconnect() {
        socket.disconnect()
    }

    companion object {
        private const val EVENT_PLAYERS_CHANGED = "players changed"
        private const val EVENT_UPDATE_SETTINGS = "update settings"
        private const val EVENT_PLAYER_BUZZER = "player buzzer"
        private const val EVENT_RESET_BUZZERS = "reset buzzers"
    }
}