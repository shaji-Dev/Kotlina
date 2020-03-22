package com.shaji.kotlina.extension.generic

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun File.toUri(context: Context, authority: String): Uri = FileProvider.getUriForFile(context, authority, this)

fun File.getFileExtension() = name.getFileExtension()