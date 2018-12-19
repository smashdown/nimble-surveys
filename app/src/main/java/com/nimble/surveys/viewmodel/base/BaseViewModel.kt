package com.nimble.surveys.viewmodel.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import com.nimble.surveys.utils.arch.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    val disposables = CompositeDisposable()

    // Common UI Events
    val finishEvent: SingleLiveEvent<Intent> = SingleLiveEvent()
    val toastLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    val toastStringLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()

    fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    @CallSuper
    override fun onCleared() {
        disposables.clear()

        super.onCleared()
    }

    open fun saveInstanceState(@NonNull outState: Bundle?) {}

    open fun restoreInstanceState(@NonNull outState: Bundle?) {}

    // for Activity
    open fun initData(intent: Intent?): Boolean {
        return true
    }

    open fun initViews(Intent: Intent?): Boolean {
        return true
    }

    // for Fragment
    open fun initData(bundle: Bundle?): Boolean {
        return true
    }

    open fun initViews(bundle: Bundle?): Boolean {
        return true
    }
}