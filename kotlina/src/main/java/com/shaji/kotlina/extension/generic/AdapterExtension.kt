package com.shaji.kotlina.extension.generic

import androidx.recyclerview.widget.RecyclerView

inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.Adapter<T>.isNotEmpty() = itemCount > 0