package com.tripper.tripper.ui.main.home
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RVItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount
        val offset = 20

        if(position == 0){
            outRect.top = offset
        } else if(position == count-1){
            outRect.bottom = offset
        }else{
            outRect.top = offset
            outRect.bottom = offset
        }
    }
}