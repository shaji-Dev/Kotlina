package com.shaji.kotlina.extension.generic

import android.app.Activity

/**
 *
 * @author Shafik Jininy<br>
 * Company: Global DPI
 * Email: shafik@blueblinkone.com
 * Date: 4/29/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.extensions.generic
 *
 */

fun Int.colorToHexAlpha(): String {
    return "#" + Integer.toHexString(this).toUpperCase()
}

fun Int.colorToHex(): String {
    return "#" + Integer.toHexString(this).toUpperCase().substring(2)
}
fun Int.toOpacity(): Float {
    return toFloat().map(0f, 255f, 0f, 1.0f)
}
fun Int.isOk() = this == Activity.RESULT_OK