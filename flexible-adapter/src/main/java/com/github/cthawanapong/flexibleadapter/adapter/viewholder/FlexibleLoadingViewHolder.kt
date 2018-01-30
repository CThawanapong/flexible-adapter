package com.github.cthawanapong.flexibleadapter.adapter.viewholder

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.ProgressBar
import com.github.cthawanapong.flexibleadapter.R


/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
class FlexibleLoadingViewHolder(private val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        @JvmStatic
        private val TAG = FlexibleLoadingViewHolder::class.java.simpleName
    }

    init {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        progressBar.indeterminateDrawable.setColorFilter(
                fetchColorAccentDark(context),
                PorterDuff.Mode.MULTIPLY)

        itemView.apply {
            when (layoutParams) {
                is StaggeredGridLayoutManager.LayoutParams -> {
                    (layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                }
            }
        }
    }

    private fun fetchColorAccentDark(context: Context): Int {
        val typedValue = TypedValue()

        val a = context.obtainStyledAttributes(typedValue.data, intArrayOf(R.attr.flexibleLoadingIndicatorColor))
        val color = a.getColor(0, 0)
        a.recycle()

        return color
    }
}