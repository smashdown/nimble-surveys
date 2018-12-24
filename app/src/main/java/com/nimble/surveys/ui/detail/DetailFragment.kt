package com.nimble.surveys.ui.detail

import com.nimble.surveys.R
import com.nimble.surveys.ui.base.BaseFragment
import com.nimble.surveys.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(DetailViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_detail
}
