package com.tfowl.cosmobuzz

import io.socket.client.Socket
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant

class RoomSettings(
    val buzzersLocked: Boolean = false,
    val firstBuzzHappened: Boolean = false,
    val onlyFirstBuzz: Boolean = false,
    val roomLocked: Boolean = false,
)

data class Player(
    val id: String,
    val text: String?,
    val username: String,
    val buzzed: Instant?,
)

class CosmoBuzzRoom(val code: String, private val socket: Socket) {
    val url: String = TODO()

    val players: StateFlow<List<Player>> = TODO()
    val settings: StateFlow<RoomSettings> = TODO()
    val buzzer: SharedFlow<Player> = TODO()

    suspend fun updateSettings(settings: RoomSettings): Unit = TODO()

    suspend fun resetBuzzers(): Unit = TODO()

    fun destroy(): Unit = TODO()
}

sealed class CreateRoomResult

data class SuccessfulRoom(val room: CosmoBuzzRoom) : CreateRoomResult()

data class FailedRoom(val cause: Throwable) : CreateRoomResult()

object CosmoBuzz {
    suspend fun createRoom(): CreateRoomResult = TODO()
}