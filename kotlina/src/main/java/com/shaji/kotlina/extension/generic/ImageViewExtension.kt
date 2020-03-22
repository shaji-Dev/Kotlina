package com.shaji.kotlina.extension.generic

import android.content.res.ColorStateList
import android.util.StateSet
import android.widget.ImageView

fun ImageView.setTintColor(color: Int) {
    imageTintList = ColorStateList(arrayOf(StateSet.NOTHING), intArrayOf(color))
}