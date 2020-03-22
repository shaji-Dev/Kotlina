package com.shaji.kotlina.extension.generic

import android.widget.EditText

fun EditText.hideKeyboard() {
    clearFocus()
    context.hideSoftKeyboard(this.windowToken)
}

// TODO
//fun EditText.openKeyboardDelayed(delayInMillis: Long = 1000) {
//    Handler().postDelayed({
//        GShowHideKeyboard(context, this).show()
//    }, delayInMillis)
//}
