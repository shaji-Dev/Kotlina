package com.shaji.kotlina.framework.generic

import android.util.SparseArray

object ObjectHolder {
    private val map = SparseArray<Any>()

    fun put(o: Any): Int {
        val key = map.size()
        map.put(key, o)
        return key
    }

    fun <T> extract(key: Int): T? {
        if(map[key] != null) {
            val o = map[key] as T
            map.remove(key)
            return o
        }
        return null
    }
}