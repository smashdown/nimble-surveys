package com.nimble.surveys.utils.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.nimble.surveys.R
import com.nimble.surveys.utils.GlideApp

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setSurveyUrl(view: ImageView, imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            val options = RequestOptions().centerCrop()

            GlideApp.with(view.context)
                .load(imageUrl)
                .apply(options)
                .dontAnimate()
                .placeholder(R.drawable.bg_image_not_available)
                .into(view)
        }
    }
}