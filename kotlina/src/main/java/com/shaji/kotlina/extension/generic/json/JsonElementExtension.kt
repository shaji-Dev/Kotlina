package com.shaji.kotlina.extension.generic.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

inline fun <reified T> JsonElement.toObject(underscorePolicy: Boolean = true): T {
    return if (underscorePolicy) {
        newGson().fromJson(this, T::class.java)
    } else {
        newGsonNoPolicy().fromJson(this, T::class.java)
    }
}

inline fun <reified T> JsonElement.toObjectList(): List<T> {
    val list = mutableListOf<T>()
    val gson = newGson()
    for (j in asJsonArray) {
        val element = gson.fromJson(j, T::class.java)
        list.add(element)
    }
    return list
}


fun newGson() = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    .create()

fun newGsonNoPolicy() = GsonBuilder().create()
