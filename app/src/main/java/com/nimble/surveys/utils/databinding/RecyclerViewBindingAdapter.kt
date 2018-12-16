package com.nimble.surveys.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.utils.views.OnScrollLoadMoreListener

object RecyclerViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("layoutManager")
    fun setAdapter(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
    }

    @JvmStatic
    @BindingAdapter("onScrollLoadMoreListener")
    fun setAdapter(recyclerView: RecyclerView, onScrollLoadMoreListener: OnScrollLoadMoreListener) {
        recyclerView.addOnScrollListener(onScrollLoadMoreListener)
    }

}