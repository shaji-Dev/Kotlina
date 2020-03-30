package com.globaldpi.measuremap.ui.activity.base

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.shaji.kotlina.extension.generic.isOk
import com.shaji.kotlina.framework.generic.Success
import org.jetbrains.anko.startActivityForResult

abstract class BaseResultActivity : AppCompatActivity() {

    internal var autoGenerateRequestCode = 1000
    internal var nextRequestCode = -1
    internal var nextResultCallback: Success<Intent>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.nextRequestCode) {
            if (resultCode.isOk() && data != null) {
                nextResultCallback?.let {
                    it(data)
                }
            } else {
                // No Result set
            }
            this.nextRequestCode = -1
            nextResultCallback = null
        }
    }

}

@Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
@PublishedApi
internal inline fun <reified T : Activity> BaseResultActivity.startForResult(vararg params: Pair<String, Any?>, requestCode: Int = -1, noinline callback: Success<Intent>) {
    this.nextRequestCode = if (requestCode == -1) autoGenerateRequestCode++ else requestCode
    this.nextResultCallback = callback
    startActivityForResult<T>(this.nextRequestCode, *params)
}

fun BaseResultActivity.startForResultIntent(intent: Intent, requestCode: Int = -1, callback: Success<Intent>) {
    this.nextRequestCode = if (requestCode == -1) autoGenerateRequestCode++ else requestCode
    this.nextResultCallback = callback
    startActivityForResult(intent, this.nextRequestCode)
}