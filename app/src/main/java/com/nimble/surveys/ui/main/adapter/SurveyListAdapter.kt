package com.nimble.surveys.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.databinding.ViewSurveyListItemBinding
import com.nimble.surveys.model.Survey
import com.nimble.surveys.ui.main.MainViewModel

open class SurveyListAdapter(
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<SurveyViewHolder>() {
    val items = mutableListOf<Survey>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewSurveyListItemBinding.inflate(inflater, parent, false)
        binding.vm = viewModel

        return SurveyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemId(position: Int): Long {
        return items[position].id.hashCode().toLong()
    }
}