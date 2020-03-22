package com.shaji.kotlina.extension.generic

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout

/**
 *
 * @author Shafik Jininy<br>
 * Company: Global DPI
 * Email: shafik@blueblinkone.com
 * Date: 4/10/2019
 * Project name: measure-map-android
 * Package name: com.globaldpi.measuremap.extensions.generic
 *
 */
fun View.getString(resId: Int): String {
    return context.getString(resId)
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isNotVisible(): Boolean {
    return visibility != View.VISIBLE
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.fadeIn() {
    visible()
    animate().alpha(1f).withEndAction { visible() }
}

fun View.fadeOut() {
    animate().alpha(0f).withEndAction { gone() }
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.disable() {
    isEnabled = false
    alpha = 0.2f
}

fun View.disable(alpha: Float) {
    isEnabled = false
    this.alpha = alpha
}

fun View.setMarginTop(upperView: View) {
    val vDownParams = layoutParams as ConstraintLayout.LayoutParams
    val leftMargin = vDownParams.leftMargin
    val rightMargin = vDownParams.rightMargin
    val bottomMargin = vDownParams.bottomMargin
    upperView.afterMeasured {
        val vUpParams = layoutParams as ConstraintLayout.LayoutParams
        vDownParams.setMargins(leftMargin, vUpParams.topMargin + height, rightMargin, bottomMargin)
        this@setMarginTop.layoutParams = vDownParams
    }
}

inline fun <T : View> T.afterMeasured(crossinline listener: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                listener()
            }
        }
    })
}

fun View.showNoResultsPerform(vararg lists: List<*>): Boolean {
    lists.iterator().forEach {
        if (it.isNotEmpty()) {
            gone()
            return false
        }
    }
    visible()
    return true
}

fun View.getMarginStart() = (layoutParams as ViewGroup.MarginLayoutParams).marginStart

fun View.setMarginStart(marginStart: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.marginStart = marginStart
        layoutParams = p
    }
}

fun View.setMarginEnd(marginEnd: Int) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.marginEnd = marginEnd
        layoutParams = p
    }
}

fun View.setWidthParam(width: Int) {
    val params = layoutParams
    if (width != params.width) {
        params.width = width
        layoutParams = params
    }
}

fun View.setHeightParam(height: Int) {
    val params = layoutParams
    if (height != params.height) {
        params.height = height
        layoutParams = params
    }
}

fun View.getWindowWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun View.getWindowHeight(): Int {
    return resources.displayMetrics.heightPixels
}


fun View.getPrimaryColor(): Int {
    return context.getPrimaryColor()
}

fun View.getAccentColor(): Int {
    return context.getAccentColor()
}