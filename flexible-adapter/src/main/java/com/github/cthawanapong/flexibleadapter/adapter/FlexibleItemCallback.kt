package com.github.cthawanapong.flexibleadapter.adapter

import android.support.v7.util.DiffUtil
import com.github.cthawanapong.flexibleadapter.model.interfaces.IFlexibleViewType

class FlexibleItemCallback : DiffUtil.ItemCallback<IFlexibleViewType>() {
    override fun areItemsTheSame(oldItem: IFlexibleViewType, newItem: IFlexibleViewType): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: IFlexibleViewType, newItem: IFlexibleViewType): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}