package com.github.cthawanapong.flexibleadapter.sample.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.cthawanapong.flexibleadapter.adapter.FlexibleAdapter

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
class ShowcaseAdapter(context: Context) : FlexibleAdapter(context) {
    companion object {
        @JvmStatic
        private val TAG = ShowcaseAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }
}