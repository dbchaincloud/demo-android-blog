package cloud.dbchain.sample.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Xiao Bo
 * @date: 10/11/2020
 */
class LinearSpacesItemDecoration(
    private val right: Int = 0,
    private val left: Int = 0,
    private val space: Int = 0,
    private val firstTop: Int = 0,
    private val lastBottom: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = left
        outRect.right = right
        outRect.top = space

        if (parent.getChildPosition(view) == 0) outRect.top = firstTop
        if (parent.getChildPosition(view) == state.itemCount - 1) outRect.bottom = lastBottom
    }
}