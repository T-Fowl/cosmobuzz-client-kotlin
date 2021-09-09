package com.tfowl.cosmobuzz

import com.tfowl.cosmobuzz.fakes.FakeCosmoSocket
import com.tfowl.cosmobuzz.fakes.FakeCosmoSocketFactory
import kotlinx.coroutines.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CosmoBuzzTests {

    @Test
    fun `creates new room`() = runBlockingTest {
        val socket = FakeCosmoSocket { req ->
            when (req) {
                is Request.CreateRoom -> "1234"
            }
        }
        val factory = FakeCosmoSocketFactory(socket)
        val cosmo = CosmoBuzz(factory)

        val room = cosmo.createRoom()
        assertTrue(room is CreateRoomResult.Success)
        assertEquals("1234", room.room.code)
    }

    @Test
    fun `handles errors`() = runBlockingTest {
        val socket = FakeCosmoSocket()
        val factory = FakeCosmoSocketFactory(socket)
        val cosmo = CosmoBuzz(factory)

        val room = cosmo.createRoom()

        assertTrue(room is CreateRoomResult.Failure)
    }
}