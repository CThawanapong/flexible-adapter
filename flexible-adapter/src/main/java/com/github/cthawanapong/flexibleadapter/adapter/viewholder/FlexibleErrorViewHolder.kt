package com.github.cthawanapong.flexibleadapter.adapter.viewholder

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.cthawanapong.flexibleadapter.R
import kotlinx.android.synthetic.main.list_item_error.view.*

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
class FlexibleErrorViewHolder(itemView: View, private val onRetryClickListener: View.OnClickListener) : RecyclerView.ViewHolder(itemView) {
    companion object {
        @JvmStatic
        private val TAG = FlexibleErrorViewHolder::class.java.simpleName
    }

    fun bindViewHolder(context: Context, @DrawableRes errorRes: Int = R.drawable.ic_error) {
        itemView.apply {
            imageViewItem.setImageResource(errorRes)
            buttonRetry.styleableButton.setOnClickListener(onRetryClickListener)
        }
    }
}