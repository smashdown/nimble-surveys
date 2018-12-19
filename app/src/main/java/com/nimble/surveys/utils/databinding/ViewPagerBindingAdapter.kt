package com.nimble.surveys.utils.databinding

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.nimble.surveys.viewmodel.main.adapter.SurveyPagerAdapter
import com.nimble.surveys.viewmodel.common.OnPageSelected
import com.nimble.surveys.viewmodel.common.VerticalViewPager
import com.viewpagerindicator.CirclePageIndicator
import timber.log.Timber

object ViewPagerBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["adapter", "onPageSelected", "indicator"], requireAll = true)
    fun setAdapter(
        view: VerticalViewPager,
        adapter: SurveyPagerAdapter,
        viewModel: OnPageSelected,
        indicator: CirclePageIndicator
    ) {
        Timber.d("setAdapter()")
        view.adapter = adapter
        view.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                viewModel.onPageSelected(position)
            }
        })
        indicator.setViewPager(view)
    }
}