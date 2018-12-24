package com.nimble.surveys.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.nimble.surveys.databinding.ViewSurveyListItemBinding
import com.nimble.surveys.model.Survey
import com.nimble.surveys.ui.main.MainViewModel

class SurveyPagerAdapter(private val viewModel: MainViewModel) : PagerAdapter() {

    val items = mutableListOf<Survey>()

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(collection.context)
        val binding = ViewSurveyListItemBinding.inflate(inflater, collection, false)
        binding.vm = viewModel
        binding.item = items[position]
        binding.root.tag = position

        collection.addView(binding.root)
        return binding.root
    }

    override fun getItemPosition(`object`: Any): Int {
        val index = items.indexOf((`object` as View).tag)
        return if (index == -1)
            PagerAdapter.POSITION_NONE
        else
            index
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) = collection.removeView(view as View)

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence? = items[position].title

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
}