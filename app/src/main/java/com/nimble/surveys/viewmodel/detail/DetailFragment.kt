package com.nimble.surveys.viewmodel.detail

import com.nimble.surveys.R
import com.nimble.surveys.viewmodel.base.BaseFragment
import com.nimble.surveys.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(DetailViewModel::class) {
    override fun getLayoutRes(): Int = R.layout.fragment_detail
}
