package com.nimble.surveys.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.databinding.ViewSurveyListItemBinding
import com.nimble.surveys.model.Survey

class SurveyViewHolder(
        private val binding: ViewSurveyListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Survey) {
        binding.item = item
        binding.executePendingBindings()
    }
}