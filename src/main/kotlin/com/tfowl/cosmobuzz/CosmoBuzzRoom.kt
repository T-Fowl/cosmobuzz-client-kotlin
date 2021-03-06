package com.tfowl.cosmobuzz

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class CosmoBuzzRoom(val code: String, private val socket: CosmoSocket) {
    val url: String = URL_FMT.format(code)

    init {
        socket.receive { event ->
            when (event) {
                is IncomingEvent.PlayersChanged      -> _players.value = event.players
                is IncomingEvent.RoomSettingsChanged -> _settings.value = event.settings
                is IncomingEvent.PlayerBuzzed        -> {
                    _players.value.find { it.id == event.id }
                        ?.let { _buzzer.tryEmit(it) }
                }
            }
        }
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

    suspend fun updateSettings(settings: RoomSettings) {
        socket.send(OutgoingEvent.UpdateRoomSettings(settings))
    }

    suspend fun resetBuzzers() {
        socket.send(OutgoingEvent.ResetBuzzers)
    }

    fun destroy() {
        _players.value = emptyList()
        _settings.value = RoomSettings()
        _buzzer.resetReplayCache()
        socket.disconnect()
    }

    override fun toString(): String = "CosmoBuzzRoom(code=$code, url=$url)"

    companion object {
        private const val BUZZER_EXTRA_CAPACITY = 10

        private const val URL_FMT = "https://www.cosmobuzz.net/#/play/%s"
    }
}