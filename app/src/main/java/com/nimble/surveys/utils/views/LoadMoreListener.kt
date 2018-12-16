package com.nimble.surveys.utils.views

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

interface LoadMoreListener : SwipeRefreshLayout.OnRefreshListener {
    fun canLoadMore(): Boolean
    fun onLoadMore()
}