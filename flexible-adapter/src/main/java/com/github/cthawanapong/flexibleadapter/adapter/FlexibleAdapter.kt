package com.github.cthawanapong.flexibleadapter.adapter

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.cthawanapong.flexibleadapter.R
import com.github.cthawanapong.flexibleadapter.adapter.viewholder.FlexibleDividerViewHolder
import com.github.cthawanapong.flexibleadapter.adapter.viewholder.FlexibleEmptyViewHolder
import com.github.cthawanapong.flexibleadapter.adapter.viewholder.FlexibleErrorViewHolder
import com.github.cthawanapong.flexibleadapter.adapter.viewholder.FlexibleLoadingViewHolder
import com.github.cthawanapong.flexibleadapter.model.FlexibleViewType
import com.github.cthawanapong.flexibleadapter.model.interfaces.FlexibleAdapterFunction
import com.github.cthawanapong.flexibleadapter.model.interfaces.FlexibleErrorCallback
import com.github.cthawanapong.flexibleadapter.model.interfaces.IFlexibleViewType
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
abstract class FlexibleAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FlexibleAdapterFunction, FlexibleErrorCallback {
    companion object {
        @JvmStatic
        private val TAG = FlexibleAdapter::class.java.simpleName

        //Static Members
        const val VIEW_TYPE_ID_DIVIDER_PADDING: Int = 500
        const val VIEW_TYPE_ID_DIVIDER: Int = 501
        const val VIEW_TYPE_ID_LOADING: Int = 502
        const val VIEW_TYPE_ID_LOADING_ITEM: Int = 503
        const val VIEW_TYPE_ID_ERROR: Int = 504
        const val VIEW_TYPE_ID_EMPTY: Int = 505
    }

    //Expose Members
    protected val VIEW_TYPE_LOADING = FlexibleViewType(VIEW_TYPE_ID_LOADING)
    protected val VIEW_TYPE_LOADING_ITEM = FlexibleViewType(VIEW_TYPE_ID_LOADING_ITEM)
    protected val VIEW_TYPE_ERROR = FlexibleViewType(VIEW_TYPE_ID_ERROR)
    protected val VIEW_TYPE_EMPTY = FlexibleViewType(VIEW_TYPE_ID_EMPTY)
    protected val VIEW_TYPE_DIVIDER_PADDING = FlexibleViewType(VIEW_TYPE_ID_DIVIDER_PADDING)
    protected val VIEW_TYPE_DIVIDER = FlexibleViewType(VIEW_TYPE_ID_DIVIDER)
    protected val listViewType: MutableList<IFlexibleViewType> = ArrayList()

    //Internal Members
    private var isLoadingShow: Boolean = false
    private var isErrorShow: Boolean = false
    private var isEmptyShow: Boolean = false
    private val eventActor by lazy { actor<List<IFlexibleViewType>>(capacity = Channel.CONFLATED) { for (list in channel) internalUpdate(list) } }
    private val diffCallback by lazy(LazyThreadSafetyMode.NONE) { DiffCallback() }
    private val onRetryClickListener by lazy { View.OnClickListener { v -> onRetryClick(v) } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ID_DIVIDER_PADDING -> FlexibleDividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_divider_padding, parent, false))
            VIEW_TYPE_ID_DIVIDER -> FlexibleDividerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_divider, parent, false))
            VIEW_TYPE_ID_LOADING -> FlexibleLoadingViewHolder(context, LayoutInflater.from(parent.context).inflate(R.layout.list_item_loading, parent, false))
            VIEW_TYPE_ID_LOADING_ITEM -> FlexibleLoadingViewHolder(context, LayoutInflater.from(parent.context).inflate(R.layout.list_item_loading_item, parent, false))
            VIEW_TYPE_ID_ERROR -> FlexibleErrorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_error, parent, false), onRetryClickListener)
            VIEW_TYPE_ID_EMPTY -> FlexibleEmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_empty, parent, false))
            else -> FlexibleEmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_empty, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewTypeId = getItemViewType(position)
        val viewType = listViewType[position]

        when (viewTypeId) {
            VIEW_TYPE_ID_ERROR -> if (holder is FlexibleErrorViewHolder) {
                holder.bindViewHolder(context)
            }
            VIEW_TYPE_ID_EMPTY -> if (holder is FlexibleEmptyViewHolder) {
                holder.bindViewHolder(context)
            }
        }
    }

    override fun getItemCount(): Int = listViewType.size

    override fun getItemViewType(position: Int): Int = listViewType[position].viewTypeId

    override fun resetAdapter() {
        listViewType.apply {
            clear()
            add(VIEW_TYPE_LOADING)
        }

        notifyDataSetChanged()
        updateInternalFlag(listViewType)
    }

    override fun showLoading() {
        bind(listOf(VIEW_TYPE_LOADING))
    }

    override fun showError() {
        bind(listOf(VIEW_TYPE_ERROR))
    }

    override fun showEmpty() {
        bind(listOf(VIEW_TYPE_EMPTY))
    }

    override val isContentEmpty: Boolean
        get() = if (isLoadingShow || isErrorShow || isEmptyShow) {
            listViewType.size == 1
        } else {
            listViewType.isEmpty()
        }

    override val isError: Boolean
        get() = isErrorShow

    override fun bind(newItemList: List<IFlexibleViewType>) {
        if (isContentEmpty) {
            listViewType.apply {
                clear()
                addAll(newItemList)
            }

            notifyDataSetChanged()
        } else {
            eventActor.offer(newItemList)
        }
    }

    private suspend fun internalUpdate(list: List<IFlexibleViewType>) {
        val result = DiffUtil.calculateDiff(diffCallback.apply { newItemList = list })
        launch(UI) {
            listViewType.apply {
                clear()
                addAll(list)
            }
            updateInternalFlag(list)
            result.dispatchUpdatesTo(this@FlexibleAdapter)
        }.join()
    }

    private fun updateInternalFlag(list: List<IFlexibleViewType>) {
        if (list.size == 1) {
            when (list.first().viewTypeId) {
                VIEW_TYPE_ID_LOADING -> {
                    isLoadingShow = true
                    isErrorShow = false
                    isEmptyShow = false
                }
                VIEW_TYPE_ID_ERROR -> {
                    isLoadingShow = false
                    isErrorShow = true
                    isEmptyShow = false
                }
                VIEW_TYPE_ID_EMPTY -> {
                    isLoadingShow = false
                    isErrorShow = false
                    isEmptyShow = true
                }
                else -> {
                    isLoadingShow = false
                    isErrorShow = false
                    isEmptyShow = false
                }
            }
        }
    }

    private inner class DiffCallback : DiffUtil.Callback() {
        lateinit var newItemList: List<IFlexibleViewType>
        override fun getOldListSize() = listViewType.size
        override fun getNewListSize() = newItemList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return listViewType[oldItemPosition].areItemsTheSame(newItemList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return listViewType[oldItemPosition].areContentsTheSame(newItemList[newItemPosition])
        }
    }

    override fun onRetryClick(view: View?) {

    }
}