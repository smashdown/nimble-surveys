package com.nimble.surveys.ui.main

import com.nimble.surveys.R
import com.nimble.surveys.base.BaseActivity
import com.nimble.surveys.databinding.ActivityMainBinding
import com.nimble.surveys.base.DummyViewModel

class MainActivity : BaseActivity<ActivityMainBinding, DummyViewModel>(DummyViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {
    }
}
