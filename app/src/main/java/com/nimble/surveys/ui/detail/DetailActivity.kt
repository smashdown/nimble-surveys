package com.nimble.surveys.ui.detail

import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import com.nimble.surveys.R
import com.nimble.surveys.ui.base.BaseActivity
import com.nimble.surveys.ui.base.DummyViewModel
import com.nimble.surveys.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity<ActivityDetailBinding, DummyViewModel>(DummyViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.activity_detail

    override fun initViews() {
        setSupportActionBar(binding.appBar.toolbar)

        val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        )

        // To put app title on center of actionBar
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setCustomView(layoutInflater.inflate(R.layout.app_title, null), params)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        val detailFragment = supportFragmentManager.findFragmentById(R.id.detailFragment) as DetailFragment?
        detailFragment?.let {
            it.viewModel.title.observe(this, Observer {
                supportActionBar?.customView?.findViewById<TextView>(R.id.tvTitle)?.text = it
            })
        }
    }

    override fun getTransitionAnimationDirection(): HSTransitionDirection {
        return HSTransitionDirection.NONE
    }
}
