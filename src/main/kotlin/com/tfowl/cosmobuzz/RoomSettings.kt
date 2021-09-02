package com.tfowl.cosmobuzz

import org.json.JSONObject

class RoomSettings(
    val buzzersLocked: Boolean = false,
    val firstBuzzHappened: Boolean = false,
    val onlyFirstBuzz: Boolean = false,
    val roomLocked: Boolean = false,
)

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