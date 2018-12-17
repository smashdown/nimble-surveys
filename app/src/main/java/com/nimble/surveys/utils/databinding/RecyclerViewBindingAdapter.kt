package com.nimble.surveys.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.utils.views.OnScrollLoadMoreListener

object RecyclerViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("onScrollLoadMoreListener")
    fun setAdapter(recyclerView: RecyclerView, onScrollLoadMoreListener: OnScrollLoadMoreListener) {
        recyclerView.addOnScrollListener(onScrollLoadMoreListener)
    }
}