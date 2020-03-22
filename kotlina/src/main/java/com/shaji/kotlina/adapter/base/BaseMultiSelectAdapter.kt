package com.shaji.kotlina.adapter.base

import android.view.View
import com.shaji.kotlina.adapter.behavior.OverSelectBehavior
import kotlinx.android.extensions.LayoutContainer

abstract class BaseMultiSelectAdapter<T>(items: MutableList<T>,
                                         private var maxSelectedPositions: Int = MAX_SELECTION_INFINITE,
                                         @OverSelectBehavior private var overSelectBehavior: Int = 0,
                                         internal var multiSelectEnabled: Boolean = false,
                                         private val listener: Listener<T>)
    : BaseAdapter<T>(items) {

    companion object {
        const val MAX_SELECTION_INFINITE = -1
        const val OVERSELECT_BEHAVIOR_NONE = 0
        const val OVERSELECT_BEHAVIOR_REMOVE_FIRST = 1
        const val OVERSELECT_BEHAVIOR_REMOVE_LAST = 2
    }

    var selectedPositions = LinkedHashMap<Int, T>()
    val selectedItems: List<T>
        get() = selectedPositions.values.toMutableList()
    val selectedCount: Int
        get() = selectedPositions.size

    interface Listener<T> {
        fun onItemClick(item: T, position: Int)
        fun onItemSelected(item: T)
        fun onItemDeselected(item: T)
        fun onAllItemsSelected()
        fun onAllItemsDeselected()
    }

    private fun setOverSelectBehavior(@OverSelectBehavior behavior: Int){
        overSelectBehavior = behavior
    }

    fun startMultiSelection() {
        multiSelectEnabled = true
        notifyDataSetChanged()
    }

    fun stopMultiSelection() {
        multiSelectEnabled = false
        clearSelection()
    }

    private fun click(item: T, position: Int) {
        listener.onItemClick(item, position)
    }

    fun selectAll(){
        selectedPositions.clear()
        items.forEachIndexed { index, item ->
            selectedPositions[index] = item
        }
        notifyDataSetChanged()
        listener.onAllItemsSelected()
    }

    fun allItemsSelected() = selectedPositions.size == items.size

    fun clearSelection() {
        selectedPositions.clear()
        notifyDataSetChanged()
        listener.onAllItemsDeselected()
    }

    fun select(position: Int, notify: Boolean = true) {
        if (multiSelectEnabled) {
            val item = items[position]
            if (!isOverSelecting()) {
                selectedPositions[position] = item
                notifyDataSetChanged()
                if (notify) {
                    listener.onItemSelected(item)
                }
            } else if(overSelectBehavior != OVERSELECT_BEHAVIOR_NONE){
                if (selectedPositions.isNotEmpty()) {
                    val keys = selectedPositions.keys
                    val toRemove = if(overSelectBehavior == OVERSELECT_BEHAVIOR_REMOVE_FIRST) keys.first() else keys.last()
                    deselect(toRemove, false)
                    select(position)
                }
            }
        }
    }

    fun deselect(position: Int, notify: Boolean = true) {
        if (multiSelectEnabled) {
            selectedPositions.remove(position)
            notifyDataSetChanged()
            if (notify) {
                listener.onItemDeselected(items[position])
            }
        }
    }

    private fun isOverSelecting(): Boolean {
        return maxSelectedPositions != MAX_SELECTION_INFINITE && selectedPositions.size >= maxSelectedPositions
    }

    fun isPositionSelected(position: Int): Boolean {
        return selectedPositions.containsKey(position)
    }

    fun setItemSelected(position: Int, select: Boolean) {
        if (select) {
            select(position)
        } else {
            deselect(position)
        }
    }

    abstract class ItemHolder<T>(itemView: View,
                                 var startOnLongPress: Boolean = false) : BaseAdapter.ItemHolder<T>(itemView), LayoutContainer {

        private lateinit var adapter: BaseMultiSelectAdapter<T>

        override fun bind(adapter: BaseAdapter<T>, item: T) {
            this.adapter = adapter as BaseMultiSelectAdapter<T>
            initListener(adapter, item)
            initSelected(adapter, item)
            bindItem(adapter, item)
        }

        abstract fun getSelectableView(): View
        abstract fun isSelectable(item: T): Boolean
        fun isSelected() = getSelectableView().isSelected
        abstract fun isSelectableWithLongClick(item: T): Boolean

        open fun onItemSelectedChanged(item: T, selected: Boolean) {}
        open fun onItemClick(item: T) {}

        abstract fun canStartMultiSelect(item: T): Boolean
        open fun onMultiSelectStartedWithLongClick(item: T) {}

        fun toggleSelect(){
            val selectableView = getSelectableView()
            if (selectableView.isSelected) {
                deselect()
            } else {
                select()
            }
        }

        fun select(){
            val selectableView = getSelectableView()
            val position = adapterPosition
            selectableView.isSelected = true
            adapter.select(position)
            adapter.getItem(position)?.let {
                onItemSelectedChanged(it, selectableView.isSelected)
            }
        }

        fun deselect(){
            val selectableView = getSelectableView()
            selectableView.isSelected = false
            val position = adapterPosition
            adapter.deselect(position)
            adapter.getItem(position)?.let {
                onItemSelectedChanged(it, selectableView.isSelected)
            }
        }

        override fun initListener(adapter: BaseAdapter<T>, item: T) = with(itemView) {
            adapter as BaseMultiSelectAdapter<T>
            val selectableView = getSelectableView()
            selectableView.setOnClickListener {
                if (adapter.multiSelectEnabled) {
                    toggleSelect()
                } else {
                    adapter.click(item, adapterPosition)
                    onItemClick(item)
                }
            }

            if (isSelectable(item) && isSelectableWithLongClick(item)) {
                getSelectableView().setOnLongClickListener {
                    var handle = false
                    if (canStartMultiSelect(item)) {
                        adapter.multiSelectEnabled = true
                        adapter.notifyDataSetChanged()
                        onMultiSelectStartedWithLongClick(item)
                        handle = true
                    }
                    handle
                }
            }
        }

        private fun initSelected(adapter: BaseMultiSelectAdapter<T>, item: T) {
            if (isSelectable(item)) {
                getSelectableView().isSelected = adapter.isPositionSelected(adapterPosition) && adapter.multiSelectEnabled
            } else {
                getSelectableView().isSelected = false
            }
        }
    }
}
