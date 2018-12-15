package com.nimble.surveys.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

object RecyclerViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        adapter?.let {
            recyclerView.adapter = it
        }
    }

    @JvmStatic
    @BindingAdapter("layoutManager")
    fun setAdapter(recyclerView: RecyclerView, layoutManager: RecyclerView.LayoutManager) {
        try {
            recyclerView.layoutManager = layoutManager
        } catch (exception: Exception) {
        }
    }

    @JvmStatic
    @BindingAdapter("isRefreshing")
    fun setAdapter(swipeRefreshLayout: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefreshing
    }
}