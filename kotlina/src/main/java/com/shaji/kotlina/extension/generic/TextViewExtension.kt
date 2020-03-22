package com.shaji.kotlina.extension.generic

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.shaji.kotlina.framework.generic.Callback

val TextView.textWatcher: TextWatcher?
    get() {
        var textWatcher: TextWatcher? = null
        tag?.let {
            if (it is TextWatcher) {
                textWatcher = it
                removeTextChangedListener(it)
            }
        }
        return textWatcher
    }

fun TextView.addTextWatcher(debounceTime: Long = 0, attachToTag: Boolean = true, listener: Callback<String>): TextWatcher {
    var handler: Handler? = null
    var runnable: Runnable? = null
    if (debounceTime > 0) {
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            listener(this.text.toString())
        }
    }

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (debounceTime > 0) {
                handler?.apply {
                    removeCallbacks(runnable)
                    postDelayed(runnable, debounceTime)
                }
            } else {
                listener(s?.toString() ?: "")
            }
        }

    }
    addTextChangedListener(textWatcher)
    if (attachToTag) {
        tag = textWatcher
    }
    return textWatcher
}

fun TextView.setTextNoWatch(text: String) {
    val textWatcher = textWatcher
    textWatcher?.let {
        removeTextChangedListener(it)
    }

    setText(text)

    textWatcher?.let {
        addTextChangedListener(textWatcher)
    }
}

fun TextView.removeTextWatcher() {
    tag?.let {
        if (it is TextWatcher) {
            removeTextChangedListener(it)
        }
    }
}