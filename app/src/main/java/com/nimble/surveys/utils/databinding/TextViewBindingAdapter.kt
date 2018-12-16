package com.nimble.surveys.utils.databinding

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

object TextViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("html")
    fun setHtml(view: TextView, html: String) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            view.text = Html.fromHtml(html)
        }
    }
}