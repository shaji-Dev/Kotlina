package com.shaji.kotlina.extension.generic.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import org.json.JSONObject

fun JSONObject.toJsonElement(): JsonElement {
    return Gson().fromJson<Any>(toString(), JsonElement::class.java) as JsonElement
}
