package com.shaji.kotlina.framework.generic

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

object BackgroundWork{
    fun run(function: VoidListener, finishListener: VoidListener){
        doAsync {
            function()
            uiThread {
                finishListener()
            }
        }

    }
}