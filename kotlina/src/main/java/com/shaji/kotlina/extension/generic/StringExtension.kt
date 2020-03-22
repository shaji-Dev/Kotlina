package com.shaji.kotlina.extension.generic

import android.graphics.Color
import android.text.Spanned
import androidx.core.text.HtmlCompat
import java.security.MessageDigest
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 *
 * @author Shafik Jininy<br>
 * Company: Global DPI
 * Email: shafik@blueblinkone.com
 * Date: 4/10/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.extensions
 *
 */
fun notBlank(vararg strings: String): Boolean {
    var stringsNotNullOrEmpty = true
    run loop@{
        strings.forEach {
            if (it.isBlank()) {
                stringsNotNullOrEmpty = false
                return@loop
            }
        }
    }
    return stringsNotNullOrEmpty
}

fun String.toIntColor(): Int {
    try {
        return Color.parseColor(this)
    } catch (e: Exception) {
    }
    return Color.BLUE
}

fun String.toColoredHtml(colorHex: String): Spanned {
    return HtmlCompat.fromHtml("<font color='$colorHex'>$this</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String.fromHtml(): Spanned {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun String.getFileExtension(): String {
    try {
        val i = lastIndexOf('.')
        if (i > 0) {
            return substring(i + 1).toLowerCase()
        }
    } catch (e: Exception) {
    }

    return ""
}

fun String.toDateString() = Date().toDateString(this)

fun String.extractDouble(): Double? {
    val p: Pattern = Pattern.compile("\\d+(\\.\\d+)?")
    val m: Matcher = p.matcher(this)
    if(m.find()){
        return m.group().toDoubleOrNull()
    }
    return null
}

fun String.sha512() = hashString("SHA-512", this)
fun String.sha256() = hashString("SHA-256", this)
fun String.sha1() = hashString("SHA-1", this)
fun String.hashString(type: String, input: String): String {
    val HEX_CHARS = "0123456789ABCDEF"
    val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}