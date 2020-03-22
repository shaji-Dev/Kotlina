package com.shaji.kotlina.extension.generic

fun Long.map(inMin: Long, inMax: Long, outMin: Long, outMax: Long): Long {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun Int.map(inMin: Int, inMax: Int, outMin: Int, outMax: Int): Int {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun Double.map(inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun Float.map(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}

fun notNull(vararg objs: Any?): Boolean {
    var notNull = true
    run loop@{
        objs.forEach {
            if(it == null){
                notNull = false
                return@loop
            }
        }
    }
    return notNull
}