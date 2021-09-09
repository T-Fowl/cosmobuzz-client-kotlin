package com.tfowl.cosmobuzz

sealed class CreateRoomResult {
    data class Success(val room: CosmoBuzzRoom) : CreateRoomResult()
    data class Failure(val cause: Throwable) : CreateRoomResult()
}


open class CosmoBuzz(private val socketFactory: CosmoSocketFactory) {
    suspend fun createRoom(): CreateRoomResult {
        return try {
            val socket = socketFactory.create()
            val code = socket.request(Request.CreateRoom)

            CreateRoomResult.Success(CosmoBuzzRoom(code, socket))
        } catch (t: Throwable) {
            CreateRoomResult.Failure(t)
        }
    }

    companion object Default : CosmoBuzz(SocketIOCosmoSocketFactory(URL))
}

private const val URL = "https://www.cosmobuzz.net:3000"
