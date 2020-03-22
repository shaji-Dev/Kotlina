package com.shaji.kotlina.extension.generic

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.shaji.kotlina.R
import org.jetbrains.anko.internals.AnkoInternals


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

inline fun <reified T : Activity> Context.createIntent(vararg params: Pair<String, Any?>): Intent {
    return AnkoInternals.createIntent(this, T::class.java, params)
}

inline fun <reified T : Activity> Context.createIntent(extras: Bundle): Intent {
    val intent = AnkoInternals.createIntent(this, T::class.java, arrayOf())
    intent.putExtras(extras)
    return intent
}

fun Context.isOrientationLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun Context.isOrientationPortrait(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun Context.getWindowWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getWindowHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun Context.getPrimaryColor(): Int {
    val typedValue = TypedValue()
    val a = obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorPrimary))
    val color = a.getColor(0, 0)
    a.recycle()
    return color

    val value = TypedValue()
    getTheme().resolveAttribute(R.attr.colorPrimary, value, true)
    return value.data
//    return resources.getColor(R.color.colorPrimary)
}

fun Context.getAccentColor(): Int {
    val typedValue = TypedValue()
    val a = obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.colorAccent))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}

fun Context.hideSoftKeyboard(view: View) {
    hideSoftKeyboard(view.windowToken)
}

fun Context.hideSoftKeyboard(token: IBinder?) {
    token?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it, 0)
    }
}

fun Context.isRtl(): Boolean {
    return Build.VERSION.SDK_INT >= 17 && resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
}

fun Context.isStoragePermissionGranted(): Boolean {
    return checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Context.checkPermissions(vararg permissions: String): Boolean {
    var allGranted = true
    run loop@{
        permissions.forEach {
            if (!checkPermission(it)) {
                allGranted = false
                return@loop
            }
        }
    }
    return allGranted
}

fun Context.checkPermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.isNetworkConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.createNotificationChannelLow(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
    }
}

fun Context.createNotificationChannelHigh(channelId: String, channelName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(channelId: String, channelName: String, importance: Int) {
    val channel = NotificationChannel(channelId, channelName, importance)
    channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
    val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    service.createNotificationChannel(channel)
}

fun Context.goToMarket(packageName: String = applicationContext.packageName) {
    try {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    } catch (ex: android.content.ActivityNotFoundException) {
        val i = Intent(Intent.ACTION_VIEW)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.data = Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
        startActivity(i)
    }
}

fun Context.openUrl(url: String) {
    try {
        val openIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(openIntent)
    } catch (ex: Exception) {
        goToMarket("com.android.chrome")
    }
}

fun Context.copyToClipboard(label: String, text: String){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
}