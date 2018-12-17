package com.nimble.surveys.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseFragment
import com.nimble.surveys.databinding.FragmentMainBinding
import com.nimble.surveys.ui.detail.DetailActivity
import com.nimble.surveys.ui.main.adapter.SurveyListAdapter

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(MainViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun initViews(bundle: Bundle?) {
        val adapter = SurveyListAdapter(viewModel)
        adapter.setHasStableIds(true)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        
        viewModel.adapter = adapter
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.navMain.observe(this, Observer {
            val options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, it, it.transitionName)
                .toBundle()

            val intent =
                Intent(activity, DetailActivity::class.java).putExtra("id", it.getTag(R.id.ivBackground) as String)
            ContextCompat.startActivity(activity!!, intent, options)
        })

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
