package com.shaji.kotlina.extension.generic

import java.text.SimpleDateFormat
import java.util.*


fun Date.toDateString() : String {
    return toDateString("MMM dd")
}

fun Date.toDateString(pattern: String) : String {
    val format = SimpleDateFormat(pattern)
    return format.format(this)
}