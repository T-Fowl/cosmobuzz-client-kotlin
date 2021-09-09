package com.tfowl.cosmobuzz

import com.tfowl.socketio.connectAwait
import com.tfowl.socketio.emitAwait
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant

private fun JSONObject.getNullableStringOrNull(key: String): String? =
    if (isNull(key) || !has(key)) null else getString(key)

private fun JSONObject.getStringOrNull(key: String): String? = if (has(key) && !isNull(key)) getString(key) else null

private fun JSONObject.getBooleanOrNull(key: String): Boolean? =
    if (has(key) && !isNull(key)) getBoolean(key) else null

private fun JSONObject.deserializePlayer(): Player? {
    val id = getStringOrNull("id") ?: return null
    val text = getNullableStringOrNull("text")
    val username = getStringOrNull("username") ?: return null
    val buzzed = getNullableStringOrNull("buzzed")?.let { Instant.parse(it) }
    return Player(id, text, username, buzzed)
}


private fun JSONObject.deserializeRoomSettings(): RoomSettings? {
    val buzzersLocked = getBooleanOrNull("buzzersLocked") ?: return null
    val firstBuzzHappened = getBooleanOrNull("firstBuzzHappened") ?: return null
    val onlyFirstBuzz = getBooleanOrNull("onlyFirstBuzz") ?: return null
    val roomLocked = getBooleanOrNull("roomLocked") ?: return null
    return RoomSettings(buzzersLocked, firstBuzzHappened, onlyFirstBuzz, roomLocked)
}

private fun RoomSettings.toJsonObject(): JSONObject = JSONObject().apply {
    put("buzzersLocked", buzzersLocked)
    put("firstBuzzHappened", firstBuzzHappened)
    put("onlyFirstBuzz", onlyFirstBuzz)
    put("roomLocked", roomLocked)
}

class SocketIOCosmoSocket(private val socket: Socket) : AbstractCosmoSocket() {

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

    override suspend fun send(event: OutgoingEvent) {
        when (event) {
            is OutgoingEvent.UpdateRoomSettings -> socket.emitAwait(
                EVENT_UPDATE_SETTINGS,
                event.settings.toJsonObject()
            )
            is OutgoingEvent.ResetBuzzers       -> socket.emitAwait(EVENT_RESET_BUZZERS)
        }
    }

    override suspend fun <T : Any> request(req: Request<T>): T {
        return when (req) {
            is Request.CreateRoom -> socket.emitAwait(REQ_CREATE_ROOM).first().toString() as T
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
        private const val REQ_CREATE_ROOM = "create room"
    }
}

class SocketIOCosmoSocketFactory(
    private val uri: String,
    private val opts: IO.Options = DEFAULT_OPTS
) : CosmoSocketFactory {

    override suspend fun create(): CosmoSocket {
        val socket = IO.socket(uri, opts).connectAwait()
        return SocketIOCosmoSocket(socket)
    }

    companion object {
        private val DEFAULT_OPTS = IO.Options().apply {
            transports = arrayOf(WebSocket.NAME)
        }
    }
}