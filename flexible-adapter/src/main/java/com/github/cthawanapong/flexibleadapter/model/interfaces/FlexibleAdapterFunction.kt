package com.github.cthawanapong.flexibleadapter.model.interfaces

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
interface FlexibleAdapterFunction {
    fun resetAdapter()

    fun showLoading()

    fun showError()

    fun showEmpty()

    val isContentEmpty: Boolean

    val isError: Boolean

    fun bind(newItemList: List<IFlexibleViewType>)
}