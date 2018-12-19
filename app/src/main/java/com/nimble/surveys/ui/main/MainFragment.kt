package com.nimble.surveys.ui.main

import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseFragment
import com.nimble.surveys.databinding.FragmentMainBinding
import com.nimble.surveys.ui.detail.DetailActivity

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(MainViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.navMain.observe(this, Observer {
            val options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, it, it.transitionName)
                .toBundle()

            val intent = Intent(activity, DetailActivity::class.java)
                .putExtra("id", it.getTag(R.id.ivBackground) as String)

            ContextCompat.startActivity(activity!!, intent, options)
        })

        viewModel.loadSurveys()
        viewModel.onRefresh()
    }
}
