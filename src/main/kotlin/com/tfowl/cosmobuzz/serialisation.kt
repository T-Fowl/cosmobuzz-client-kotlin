package com.tfowl.cosmobuzz

import org.json.JSONObject

internal fun JSONObject.getNullableStringOrNull(key: String): String? =
    if (isNull(key) || !has(key)) null else getString(key)

internal fun JSONObject.getStringOrNull(key: String): String? = if (has(key) && !isNull(key)) getString(key) else null

internal fun JSONObject.getBooleanOrNull(key: String): Boolean? =
    if (has(key) && !isNull(key)) getBoolean(key) else null