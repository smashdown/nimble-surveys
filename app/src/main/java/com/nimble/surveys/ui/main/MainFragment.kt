package com.nimble.surveys.ui.main

import android.os.Bundle
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseFragment
import com.nimble.surveys.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding, MainViewModel>(MainViewModel::class) {
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_main

    override fun initViews(bundle: Bundle?) {
    }

    override fun observeViewModel() {
        super.observeViewModel()
    }
}
