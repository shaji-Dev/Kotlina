package com.shaji.kotlina.extension.generic

import java.math.BigDecimal
import java.math.BigInteger

fun BigInteger.toNumberString(): String {
    return "%,d".format(this)
}

fun Float.toNumberString(digits: Int): String {
    return "%,.${digits}f".format(this)
}

fun Int.toNumberString(): String {
    return "%,d".format(this)
}

fun Long.toNumberString(): String {
    return "%,d".format(this)
}

fun BigDecimal.toNumberString(digits: Int): String {
    return "%,.${digits}f".format(this.toDouble())
}

fun Double.toNumberString(digits: Int): String {
    return "%,.${digits}f".format(this)
}

fun notNan(vararg numbers: Double): Boolean {
    var notNan = true
    run loop@{
        for(n in numbers){
            if(n == Double.NaN){
                notNan = false
                return@loop
            }
        }
    }
    return notNan
}