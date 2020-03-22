package com.shaji.kotlina.extension.generic

fun StackTraceElement.generateTag() = className.substringAfterLast(".")

fun StackTraceElement.generateMessage(msg: Any) = "$methodName() $msg"
