@file:OptIn(ExperimentalTime::class)

package com.tfowl.cosmobuzz

import app.cash.turbine.test
import com.tfowl.cosmobuzz.fakes.FakeCosmoSocket
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.ExperimentalTime

class CosmoBuzzRoomTests {

    @Test
    fun `correct room url`() {
        val room = CosmoBuzzRoom(code = "1234", FakeCosmoSocket())

        assertEquals("https://www.cosmobuzz.net/#/play/1234", room.url)
    }

    @Test
    fun `incoming events update state`() {
        val socket = FakeCosmoSocket()
        val room = CosmoBuzzRoom(code = "1234", socket)

        // NOT the default
        val settings = RoomSettings(
            buzzersLocked = true,
            firstBuzzHappened = true,
            onlyFirstBuzz = true,
            roomLocked = true
        )
        socket.emit(IncomingEvent.RoomSettingsChanged(settings))
        assertEquals(settings, room.settings.value)

        val players = listOf(
            Player("1", null, "abc", null),
        )
        socket.emit(IncomingEvent.PlayersChanged(players))
        assertEquals(players, room.players.value)

        // HAS to come after PlayersChanged event above
        runBlockingTest {
            room.buzzer.test {
                socket.emit(IncomingEvent.PlayerBuzzed(players.first().id))
                assertEquals(players.first(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    }

    @Test
    fun `correctly looks up buzzing players`() = runBlockingTest {
        val socket = FakeCosmoSocket()
        val room = CosmoBuzzRoom(code = "1234", socket)

        val players = listOf(
            Player("1", null, "abc", null),
            Player("2", null, "def", null),
            Player("3", null, "ghi", null),
        )
        socket.emit(IncomingEvent.PlayersChanged(players))

        room.buzzer.test {
            socket.emit(IncomingEvent.PlayerBuzzed(players.last().id))
            assertEquals(players.last(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state`() {
        val room = CosmoBuzzRoom(code = "1234", FakeCosmoSocket())

        assertTrue(room.players.value.isEmpty())
        assertTrue(room.buzzer.replayCache.isEmpty())
    }

    @Test
    fun `events correctly sent`() = runBlockingTest {
        val socket = FakeCosmoSocket()
        val room = CosmoBuzzRoom(code = "1234", socket)

        assertTrue(socket.sent.isEmpty())

        room.updateSettings(RoomSettings())
        assertTrue(socket.sent.single() is OutgoingEvent.UpdateRoomSettings)
        socket.sent.clear()

        room.resetBuzzers()
        assertTrue(socket.sent.single() is OutgoingEvent.ResetBuzzers)
        socket.sent.clear()
    }
}