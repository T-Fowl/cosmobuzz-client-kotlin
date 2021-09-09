package com.tfowl.cosmobuzz.example

import com.tfowl.cosmobuzz.CosmoBuzz
import com.tfowl.cosmobuzz.CosmoBuzzRoom
import com.tfowl.cosmobuzz.CreateRoomResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

suspend fun run(room: CosmoBuzzRoom) {
    println(room)

    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch(Dispatchers.IO) {
        room.buzzer.collect { player ->
            println("BUZZ: ${player.username}")
        }
    }


    scope.launch(Dispatchers.Default) {
        val scanner = Scanner(System.`in`)
        while (scanner.hasNext()) {
            when (scanner.nextLine().trim()) {
                "?"                -> {
                    println("list-players, show-settings, reset-buzzers, toggle-first-buz")
                }
                "list-players"     -> {
                    val players = room.players.value
                    println(players.joinToString(
                        prefix = "Players (${players.size}):\n",
                        separator = "\n",
                        transform = { player -> "\t${player.username}" }
                    ))
                }
                "show-settings"    -> {
                    println(room.settings.value)
                }
                "reset-buzzers"    -> room.resetBuzzers()
                "toggle-first-buz" -> {
                    val settings = room.settings.value
                    val toggled = settings.copy(onlyFirstBuzz = !settings.onlyFirstBuzz)
                    room.updateSettings(toggled)
                }
            }
        }
    }
}

suspend fun main() {
    when (val result = CosmoBuzz.createRoom()) {
        is CreateRoomResult.Success -> run(result.room)
        is CreateRoomResult.Failure -> {
            println("An error occurred creating a room")
            result.cause.printStackTrace()
        }
    }
}