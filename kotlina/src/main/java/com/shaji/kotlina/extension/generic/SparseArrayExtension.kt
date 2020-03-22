package com.shaji.kotlina.extension.generic

import android.util.SparseArray

/**
 *
 * @author Shafik Jininy<br>
 * Company: Blue Blink One
 * Email: shaji@blueblinkone.com
 * Date: 5/18/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.extensions.generic
 *
 */
inline fun <reified T : Any> SparseArray<T>.toMutableList(): MutableList<T> {
    val list = mutableListOf<T>()
    this.forEach { item ->
        list.add(item)
    }
    return list
}
inline fun <reified T : Any> SparseArray<T>.isEmpty(): Boolean {
    return size() == 0
}

inline fun <reified T : Any> SparseArray<T>.forEach(listener: ForEachListener<T>) {
    for (index in 0 until this.size()) {
        listener(this.valueAt(index), keyAt(index), index)
    }
}
inline fun <reified T : Any> SparseArray<T>.forEach(listener: ForEachListener2<T>) {
    for (index in 0 until this.size()) {
        listener(this.valueAt(index))
    }
}
typealias ForEachListener<T> = (T, Int, Int) -> Unit
typealias ForEachListener2<T> = (T) -> Unit