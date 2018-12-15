package com.nimble.surveys.utils.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nimble.surveys.GlideApp
import com.nimble.surveys.R
import com.nimble.surveys.model.Survey
import com.nimble.surveys.ui.main.MainViewModel

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["survey", "viewModel"], requireAll = true)
    fun setImageUrl(view: ImageView, survey: Survey, viewModel: MainViewModel) {
        if (survey.coverImageUrl.isNotEmpty() && survey.coverImageAvailable) {
            GlideApp.with(view.context)
                    .load(survey.coverImageUrl + "l")
                    .placeholder(R.drawable.bg_image_not_available)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            viewModel.onImageLoadFailed(survey)
                            return true
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            if (isFirstResource)
                                view.setImageDrawable(resource)
                            return true
                        }
                    })
                    .into(view)
        }
    }
}