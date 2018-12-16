package com.nimble.surveys.ui.detail

import android.os.Bundle
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseFragment
import com.nimble.surveys.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(DetailViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_detail

    override fun initViews(bundle: Bundle?) {

    }

    override fun observeViewModel() {
        super.observeViewModel()

    }
}
