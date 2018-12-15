package com.nimble.surveys.ui.main

import androidx.fragment.app.Fragment
import com.nimble.surveys.base.BaseViewModel
import com.nimble.surveys.model.common.Status
import com.nimble.surveys.utils.arch.SingleLiveEvent

class MainViewModel(
        private val fragment: Fragment
) : BaseViewModel() {
    val status: SingleLiveEvent<Status> = SingleLiveEvent(Status.EMPTY)

    fun loadSurveys() {

    }

    fun onRefresh() {

    }

    fun onClickMenu() {

    }
}
