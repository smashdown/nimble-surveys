package com.nimble.surveys.ui.main

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseFragment
import com.nimble.surveys.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(MainViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun initViews(bundle: Bundle?) {

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.indicatorCountChanged.observe(this, Observer { itemCount ->
            // Set up the indicator
            binding.viewIndicator.itemCount = itemCount
            binding.viewIndicator.recyclerView = binding.recyclerView
            binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (!recyclerView.canScrollVertically(1)) {
                        val position =
                                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        binding.viewIndicator.currentPosition = position
                    } else {
                        val position =
                                (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                        binding.viewIndicator.currentPosition = position
                    }
                }
            })
            binding.viewIndicator.updateIndicator()
        })
        viewModel.loadSurveys()
        viewModel.onRefresh()
    }
}
