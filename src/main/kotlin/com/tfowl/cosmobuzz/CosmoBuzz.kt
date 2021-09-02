package com.tfowl.cosmobuzz

import io.socket.client.Socket
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


class CosmoBuzzRoom(val code: String, private val socket: Socket) {
    val url: String = URL_FMT.format(code)

    init {
        socket.on(EVENT_PLAYERS_CHANGED) { args -> }
        socket.on(EVENT_UPDATE_SETTINGS) { args -> }
        socket.on(EVENT_PLAYER_BUZZER) { args -> }
    }

    private val _players = MutableStateFlow(emptyList<Player>())
    val players: StateFlow<List<Player>> = _players

    private val _settings = MutableStateFlow(RoomSettings())
    val settings: StateFlow<RoomSettings> = _settings

    private val _buzzer = MutableSharedFlow<Player>(
        extraBufferCapacity = BUZZER_EXTRA_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val buzzer: SharedFlow<Player> = _buzzer

    suspend fun updateSettings(settings: RoomSettings): Unit = TODO()

    suspend fun resetBuzzers(): Unit = TODO()

    fun destroy(): Unit = TODO()

    override fun toString(): String = "CosmoBuzzRoom(code=$code, url=$url)"

    companion object {
        private const val EVENT_PLAYERS_CHANGED = "players changed"
        private const val EVENT_UPDATE_SETTINGS = "update settings"
        private const val EVENT_PLAYER_BUZZER = "player buzzer"
        private const val EVENT_RESET_BUZZERS = "reset buzzers"

        private const val BUZZER_EXTRA_CAPACITY = 10

        private const val URL_FMT = "https://www.cosmobuzz.net/#/play/%s"
    }
}

sealed class CreateRoomResult

data class SuccessfulRoom(val room: CosmoBuzzRoom) : CreateRoomResult()

data class FailedRoom(val cause: Throwable) : CreateRoomResult()

object CosmoBuzz {
    suspend fun createRoom(): CreateRoomResult = TODO()
}