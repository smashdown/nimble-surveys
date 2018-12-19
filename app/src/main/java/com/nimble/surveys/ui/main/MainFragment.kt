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
    companion object {
        val ARG_ID = "id"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.navMain.observe(this, Observer { view ->
            activity?.let {
                val options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(it, view, view.transitionName)
                    .toBundle()

                val intent = Intent(it, DetailActivity::class.java)
                    .putExtra(ARG_ID, view.getTag(R.id.ivBackground) as String)

                ContextCompat.startActivity(it, intent, options)
            }
        })

        viewModel.loadSurveys()
        viewModel.onRefresh()
    }
}
