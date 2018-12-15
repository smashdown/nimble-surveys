package com.nimble.surveys.utils.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
//import com.nimble.surveys.GlideApp
import com.nimble.surveys.di.NetworkProperties
import timber.log.Timber

object ImageViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageId")
    fun setImageId(view: ImageView, imageId: String) {
        Timber.d("setImageId() - imageId=%s, %s", imageId, NetworkProperties.SERVER_URL + "images/$imageId")

        if (imageId.isNotEmpty()) {
//            GlideApp.with(view.context)
//                    .load(NetworkProperties.SERVER_URL + "images/$imageId")
//                    .into(view)
        }
    }

}