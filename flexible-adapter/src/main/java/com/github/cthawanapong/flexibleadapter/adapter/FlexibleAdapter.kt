package com.github.cthawanapong.flexibleadapter.adapter

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
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

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
abstract class FlexibleAdapter(val context: Context) : ListAdapter<IFlexibleViewType, RecyclerView.ViewHolder>(FlexibleItemCallback()), FlexibleAdapterFunction, FlexibleErrorCallback {
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

    //Internal Members
    private var isLoadingShow: Boolean = false
    private var isErrorShow: Boolean = false
    private var isEmptyShow: Boolean = false
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
        val viewType = getItem(position)

        when (viewTypeId) {
            VIEW_TYPE_ID_ERROR -> if (holder is FlexibleErrorViewHolder) {
                holder.bindViewHolder(context)
            }
            VIEW_TYPE_ID_EMPTY -> if (holder is FlexibleEmptyViewHolder) {
                holder.bindViewHolder(context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).viewTypeId

    override fun resetAdapter() {
        submitList(listOf(VIEW_TYPE_LOADING))
        updateInternalFlag()
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
            itemCount == 1
        } else {
            itemCount == 0
        }

    override val isError: Boolean
        get() = isErrorShow

    override fun bind(newItemList: List<IFlexibleViewType>) {
        submitList(newItemList)
    }

    private fun updateInternalFlag() {
        if (itemCount == 1) {
            when (getItem(itemCount - 1).viewTypeId) {
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

    override fun onRetryClick(view: View?) {

    }
}