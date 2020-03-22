package com.shaji.kotlina.extension.generic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.shaji.kotlina.R

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
fun <T: ViewDataBinding> Fragment.bindData(layoutId: Int): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
}
fun Fragment.inflate(layoutId: Int): View {
    return View.inflate(context, layoutId, null)
}

fun Fragment.withActivity(callback: (FragmentActivity) -> Unit) = activity?.let {
    callback(it)
}

fun Fragment.hideSoftKeyboard() {
    activity?.let { ac ->
        val v = ac.currentFocus
        if (v != null) {
            val imm = ac.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}

