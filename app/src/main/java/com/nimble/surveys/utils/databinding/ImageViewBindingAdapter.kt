package com.nimble.surveys.utils.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.nimble.surveys.GlideApp
import com.nimble.surveys.R

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setSurveyUrl(view: ImageView, imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            GlideApp.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.bg_image_not_available)
                    .into(view)
        }
    }

}