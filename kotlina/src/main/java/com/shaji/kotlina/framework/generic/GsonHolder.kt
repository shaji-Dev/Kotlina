package com.shaji.kotlina.framework.generic

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 *
 * @author Shafik Jininy<br>
 * Company: Blue Blink One
 * Email: shaji@blueblinkone.com
 * Date: 5/1/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.framework.generic
 *
 */
object GsonHolder {

    private val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    private val gsonNoPolicy = GsonBuilder().create()

    fun getGson(): Gson = gson
    fun getGsonNoPolicy(): Gson = gsonNoPolicy
}