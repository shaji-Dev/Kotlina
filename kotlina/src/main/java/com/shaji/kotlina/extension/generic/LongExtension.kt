package com.shaji.kotlina.extension.generic

import java.text.SimpleDateFormat

fun Long.toDateString() : String {
    return toDateString("MMM dd")
}

fun Long.toDateString(pattern: String) : String {
    val format = SimpleDateFormat(pattern)
    return format.format(this)
}