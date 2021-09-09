package com.tfowl.cosmobuzz

import com.tfowl.socketio.connectAwait
import com.tfowl.socketio.emitAwait
import io.socket.client.IO
import io.socket.engineio.client.transports.WebSocket

sealed class CreateRoomResult {
    data class Success(val room: CosmoBuzzRoom) : CreateRoomResult()
    data class Failure(val cause: Throwable) : CreateRoomResult()
}


open class CosmoBuzz {
    suspend fun createRoom(): CreateRoomResult {
        return try {
            val opts = IO.Options().apply {
                transports = arrayOf(WebSocket.NAME)
            }
            val socket = IO.socket(URL, opts).connectAwait()

            val code = socket.emitAwait(CREATE_ROOM).first().toString()

            CreateRoomResult.Success(CosmoBuzzRoom(code, SocketIOCosmoSocket(socket)))
        } catch (t: Throwable) {
            CreateRoomResult.Failure(t)
        }
    }

    companion object Default : CosmoBuzz() {
        private const val URL = "https://www.cosmobuzz.net:3000"
        private const val CREATE_ROOM = "create room"
    }
}