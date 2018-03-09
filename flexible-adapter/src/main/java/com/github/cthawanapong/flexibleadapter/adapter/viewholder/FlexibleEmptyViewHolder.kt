package com.github.cthawanapong.flexibleadapter.adapter.viewholder

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.cthawanapong.flexibleadapter.R
import kotlinx.android.synthetic.main.list_item_empty.view.*

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
class FlexibleEmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        @JvmStatic
        private val TAG = FlexibleEmptyViewHolder::class.java.simpleName
    }

    fun bindViewHolder(context: Context, @DrawableRes emptyRes: Int = R.drawable.ic_empty_box) {
        itemView.apply {
            imageViewEmpty.setImageResource(emptyRes)
        }
    }
}