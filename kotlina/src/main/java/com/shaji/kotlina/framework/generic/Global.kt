package com.shaji.kotlina.framework.generic

import android.os.Looper

fun isOnMainThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

inline fun <reified INNER> array2d(sizeOuter: Int, sizeInner: Int, noinline innerInit: (Int) -> INNER): Array<Array<INNER>>
        = Array(sizeOuter) { Array<INNER>(sizeInner, innerInit) }

inline fun <reified INNER> array3d(size1: Int, size2: Int, size3: Int, noinline innerInit: (Int) -> INNER): Array<Array<Array<INNER>>>
        = Array(size1) { Array(size2) { Array<INNER>(size3, innerInit) } }

fun array3dFloat(size1: Int, size2: Int, size3: Int): Array<Array<FloatArray>>
        = Array(size1) { Array(size2) { FloatArray(size3) } }

infix fun <T> Array<T>.isIn(array: Array<Array<T>>): Boolean {
    return array.any { it.contentEquals(this) }
}