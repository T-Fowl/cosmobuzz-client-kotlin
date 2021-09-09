package com.tfowl.cosmobuzz

data class RoomSettings(
    val buzzersLocked: Boolean = false,
    val firstBuzzHappened: Boolean = false,
    val onlyFirstBuzz: Boolean = false,
    val roomLocked: Boolean = false,
)