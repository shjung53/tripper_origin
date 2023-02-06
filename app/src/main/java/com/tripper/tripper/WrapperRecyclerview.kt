package com.tripper.tripper

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class WrapperRecyclerview(context: Context, attributeSet: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attributeSet, defStyle) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_SETTLING) {
            this.stopScroll();
        }
    }
}
