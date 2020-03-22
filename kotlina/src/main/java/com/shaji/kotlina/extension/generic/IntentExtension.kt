package com.shaji.kotlina.extension.generic

import android.app.Activity
import android.content.Intent

/**
 *
 * @author Shafik Jininy<br>
 * Company: Global DPI
 * Email: shafik@blueblinkone.com
 * Date: 4/13/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.extensions.generic
 *
 */

fun Intent.startActivity(activity: Activity) {
    activity.startActivity(this)
}

fun Intent.startActivityForResult(activity: Activity, requestCode: Int) {
    activity.startActivityForResult(this, requestCode)
}