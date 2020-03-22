package com.shaji.kotlina.adapter.base

typealias OnItemClickListener<T> = (holder: BaseAdapter.ItemHolder<T>, item: T, position: Int) -> Unit