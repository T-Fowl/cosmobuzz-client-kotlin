package com.tfowl.cosmobuzz

sealed class CreateRoomResult

data class SuccessfulRoom(val room: CosmoBuzzRoom) : CreateRoomResult()

data class FailedRoom(val cause: Throwable) : CreateRoomResult()

object CosmoBuzz {
    suspend fun createRoom(): CreateRoomResult = TODO()
}