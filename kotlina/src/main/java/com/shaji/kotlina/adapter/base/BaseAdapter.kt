package com.shaji.kotlina.adapter.base


import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.shaji.kotlina.R
import com.shaji.kotlina.extension.generic.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_progress.*

abstract class BaseAdapter<T>(items: MutableList<T>,
                              private var footerEnabled: Boolean = false,
                              private var headerEnabled: Boolean = false) : RecyclerView.Adapter<BaseAdapter.Holder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_HEADER = -1
        private const val VIEW_TYPE_FOOTER = -2
        private const val VIEW_TYPE_PAGINATION_PROGRESS = -3

        private const val PROGRESS_POSITION_START = 0
        private const val PROGRESS_POSITION_END = 1
    }

    private var onItemClickListener: OnItemClickListener<T>? = null
    private var isPaginationProgressEnabled: Boolean = false
    private var paginationProgressPosition = PROGRESS_POSITION_END

    /**
     * Use this property to get or set the items, setting new items here will take care of the notifyDataSetChanged too
     */
    var items: MutableList<T> = mutableListOf()
        set(newData) {
            paginationProgressPosition = 0
            val reset = field.size == 0
            field = newData

            notifyDataSetChanged()
//            if (reset) {
//                this.notifyDataSetChanged()
//            } else {
//                applyAndAnimateRemovals(newData)
//                applyAndAnimateAdditions(newData)
//                applyAndAnimateMovedItems(newData)
//            }
        }

    init {
        this.items = items
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return when (viewType) {
            VIEW_TYPE_PAGINATION_PROGRESS -> onCreatePaginationProgressViewHolder(parent)
            VIEW_TYPE_HEADER -> onCreateHeaderViewHolder(parent)
            VIEW_TYPE_FOOTER -> onCreateFooterViewHolder(parent)
            else -> onCreateItemViewHolder(parent, viewType)
        }
    }

    abstract fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T>

    open fun onCreatePaginationProgressViewHolder(parent: ViewGroup): PaginationProgressViewHolder {
        return PaginationProgressViewHolder(parent.inflate(R.layout.item_progress))
    }

    open fun onCreateHeaderViewHolder(parent: ViewGroup): HeaderHolder<T> {
        throw Exception("You need to override this method")
    }

    open fun onCreateFooterViewHolder(parent: ViewGroup): FooterHolder<T> {
        throw Exception("You need to override this method")
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_PAGINATION_PROGRESS -> {
                (holder as PaginationProgressViewHolder).show()
            }
            VIEW_TYPE_HEADER -> {
                (holder as HeaderHolder<T>).bind(this)
            }
            VIEW_TYPE_FOOTER -> {
                (holder as FooterHolder<T>).bind(this)
            }
            else -> {
                var itemPos = position
                if (headerEnabled) {
                    itemPos--
                }
                if (isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_START) {
                    itemPos--
                }
                val item = items[itemPos]
                (holder as ItemHolder<T>).bind(this, item)
            }
        }
    }

    override fun getItemCount(): Int {
        var count = items.size
        if (isPaginationProgressEnabled) {
            count++
        }
        if (headerEnabled) {
            count++
        }
        if (footerEnabled) {
            count++
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (isPaginationProgressEnabled) {
            if (paginationProgressPosition == PROGRESS_POSITION_END && position >= itemCount || paginationProgressPosition == PROGRESS_POSITION_START && position == 0) {
                return VIEW_TYPE_PAGINATION_PROGRESS
            }
        }
        if (headerEnabled && isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_START && position == 1 || headerEnabled && position == 0) {
            return VIEW_TYPE_HEADER
        }
        return if (footerEnabled && isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_END && position == itemCount - 2 || footerEnabled && position == itemCount - 1) {
            VIEW_TYPE_FOOTER
        } else getItemType(position)
    }

    /**
     * Override this method if the adapter has multiple view types
     *
     * @param position the item position to check its view type
     * @return the view type of
     */
    open fun getItemType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    /**
     * Enable or disable footer (Default is true)
     *
     * Do not set to true when loading initial data, set this to true when loading the next page
     *
     * @param isEnabled to turn on or off footer.
     */
    fun setProgressEnabled(isEnabled: Boolean) {
        this.setProgressEnabled(isEnabled, paginationProgressPosition)
    }


    /**
     * Enable or disable footer (Default is true)
     *
     * Do not set to true when loading initial data, set this to true when loading the next page
     *
     * @param isEnabled to turn on or off footer.
     */
    fun setProgressEnabled(isEnabled: Boolean, progressPosition: Int) {
        this.paginationProgressPosition = progressPosition
        if (this.isPaginationProgressEnabled != isEnabled) {
            if (isEnabled) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
        }
        this.isPaginationProgressEnabled = isEnabled
    }

    fun setHeaderViewEnabled(isHeaderEnabled: Boolean) {
        if (isHeaderEnabled != headerEnabled) {
            val position = if (isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_START) 1 else 0
            if (isHeaderEnabled) {
                notifyItemInserted(position)
            } else {
                notifyItemRemoved(position)
            }
        }
        this.headerEnabled = isHeaderEnabled
    }

    fun setFooterViewEnabled(isFooterEnabled: Boolean) {
        if (isFooterEnabled != footerEnabled) {
            val position = if (isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_END) itemCount - 1 else itemCount - 2
            if (isFooterEnabled) {
                notifyItemInserted(position)
            } else {
                notifyItemRemoved(position)
            }
        }
        this.footerEnabled = isFooterEnabled
    }

    fun getItem(position: Int): T? {
        return if (items.size > position) {
            items[position]
        } else null
    }

    fun addItems(toAdd: MutableList<T>) {
        val index = items.size
        items.addAll(toAdd)
        notifyItemRangeInserted(index, toAdd.size)
    }

    fun addItems(index: Int, toAdd: MutableList<T>) {
        items.addAll(index, toAdd)
        notifyItemRangeInserted(index, toAdd.size)
    }

    open fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun addItem(position: Int, item: T) {
        items.add(position, item)
        notifyItemInserted(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        var tp = toPosition
        val item = items.removeAt(fromPosition)
        val count = itemCount
        if (tp > count) tp = count
        items.add(tp, item)
        notifyItemMoved(fromPosition, tp)
    }

    fun removeItem(position: Int): T? {
        if (position in 0 until itemCount) {
            val removed = items.removeAt(position)
            notifyItemRemoved(position)
            return removed
        }
        return null
    }

    private fun applyAndAnimateRemovals(newData: MutableList<T>) {
        for (i in items.indices.reversed()) {
            val model = items[i]
            if (!newData.contains(model)) {
                removeItem(i)
            }
        }
    }

    private fun applyAndAnimateAdditions(newData: MutableList<T>) {
        var i = 0
        val count = newData.size
        while (i < count) {
            val model = newData[i]
            if (!items.contains(model)) {
                addItem(i, model)
            }
            i++
        }
    }

    private fun applyAndAnimateMovedItems(newData: MutableList<T>) {
        for (toPosition in newData.indices.reversed()) {
            val model = newData[toPosition]
            val fromPosition = items.indexOf(model)
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition)
            }
        }
    }

    private fun onItemClick(holder: ItemHolder<T>, item: T, adapterPosition: Int) {
        onItemClickListener?.let {
            var position = adapterPosition
            if (headerEnabled) position--
            if (isPaginationProgressEnabled && paginationProgressPosition == PROGRESS_POSITION_START) position--

            it(holder, item, position)
        }
    }

    abstract class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        val context: Context
            get() = itemView.context
        override val containerView: View?
            get() = itemView

        fun getString(@StringRes resId: Int): String {
            return context.getString(resId)
        }
    }

    open class PaginationProgressViewHolder(v: View) : Holder(v) {
        fun show() {
            pb.show()
        }

        fun hide() {
            pb.hide()
        }
    }

    abstract class HeaderHolder<T>(itemView: View) : Holder(itemView) {
        abstract fun bind(adapter: BaseAdapter<T>)
    }

    abstract class FooterHolder<T>(itemView: View) : Holder(itemView) {
        abstract fun bind(adapter: BaseAdapter<T>)
    }

    abstract class ItemHolder<T>(itemView: View) : Holder(itemView) {
        internal open fun bind(adapter: BaseAdapter<T>, item: T) {
            initListener(adapter, item)
            bindItem(adapter, item)
        }

        abstract fun bindItem(adapter: BaseAdapter<T>, item: T)

        protected open fun initListener(adapter: BaseAdapter<T>, item: T) {
            itemView.setOnClickListener { adapter.onItemClick(this@ItemHolder, item, adapterPosition) }
        }
    }
}
