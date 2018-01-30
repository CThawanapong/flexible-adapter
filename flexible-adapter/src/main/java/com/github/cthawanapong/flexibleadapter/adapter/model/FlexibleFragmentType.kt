package com.github.cthawanapong.flexibleadapter.adapter.model

import com.github.cthawanapong.flexibleadapter.adapter.model.interfaces.IFlexibleFragmentType

/**
 * Created by CThawanapong on 30/1/2018 AD.
 * Email: c.thawanapong@gmail.com
 */
data class FlexibleFragmentType(
        override var fragmentTypeId: Long = -1,
        override val fragmentTitle: String = ""
) : IFlexibleFragmentType