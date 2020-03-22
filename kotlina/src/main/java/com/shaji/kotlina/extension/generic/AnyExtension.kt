package com.shaji.kotlina.extension.generic

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject

fun Any.toJSONObject(): JSONObject {
    return JSONObject(Gson().toJson(this, Any::class.java))
}

fun Any.toJSONString(): String {
    return Gson().toJson(this, Any::class.java)
}

fun Any.toJSONArray(): JSONArray {
    return JSONArray(Gson().toJson(this, Any::class.java))
}

fun Any.toJsonElement(): JsonElement {
    return JsonParser().parse(Gson().toJson(this))
}

fun List<Any>.toJsonElement(): JsonElement {
    return JsonParser().parse(Gson().toJson(this))
}

inline fun <reified T : Any> List<*>.checkItemsAre() = all { it is T }
