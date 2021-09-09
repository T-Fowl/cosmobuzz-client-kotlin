package com.tfowl.cosmobuzz

import java.time.Instant


data class Player(
    val id: String,
    val text: String?,
    val username: String,
    val buzzed: Instant?,
)
