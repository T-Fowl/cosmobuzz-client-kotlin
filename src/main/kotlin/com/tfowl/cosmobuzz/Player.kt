package com.tfowl.cosmobuzz

import org.json.JSONObject
import java.time.Instant


data class Player(
    val id: String,
    val text: String?,
    val username: String,
    val buzzed: Instant?,
)

private fun JSONObject.deserializePlayer(): Player? {
    val id = getStringOrNull("id") ?: return null
    val text = getNullableStringOrNull("text")
    val username = getStringOrNull("username") ?: return null
    val buzzed = getNullableStringOrNull("buzzed")?.let { Instant.parse(it) }
    return Player(id, text, username, buzzed)
}
