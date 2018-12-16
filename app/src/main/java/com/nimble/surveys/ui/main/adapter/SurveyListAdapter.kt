package com.nimble.surveys.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.databinding.ViewSurveyListItemBinding
import com.nimble.surveys.model.Survey
import com.nimble.surveys.ui.main.MainViewModel

class SurveyListAdapter(
        private val viewModel: MainViewModel,
        private val items: List<Survey>
) : RecyclerView.Adapter<SurveyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewSurveyListItemBinding.inflate(inflater, parent, false)
        binding.vm = viewModel

        return SurveyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) = holder.bind(items[position])
}