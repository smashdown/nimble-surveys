package com.nimble.surveys.utils.views

import androidx.recyclerview.widget.RecyclerView

class OnScrollLoadMoreListener(
    private val loadMoreListener: LoadMoreListener,
    private val isReverse: Boolean
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (!recyclerView.canScrollVertically(if (isReverse) -1 else 1)) {
            if (loadMoreListener.canLoadMore()) {
                loadMoreListener.onLoadMore()
            }
        } else if (dy < 0) {
            // Scrolled Up
        } else if (dy > 0) {
            // Scrolled Down
        }
    }
}