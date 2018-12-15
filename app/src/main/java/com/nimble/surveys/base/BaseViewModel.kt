package com.nimble.surveys.base

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
    val apiErrorEvent: SingleLiveEvent<Throwable> = SingleLiveEvent()
    val finishEvent: SingleLiveEvent<Intent> = SingleLiveEvent() // succeed
    //    val permissionRequestEvent: SingleLiveEvent<PermissionRequestParam> = SingleLiveEvent()
    val toastLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    val snackBarLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()
    val progressDialogLiveEvent: SingleLiveEvent<String> = SingleLiveEvent()
    //    val confirmDialogLiveEvent: SingleLiveEvent<ConfirmDialogParam> = SingleLiveEvent()
    //    val simpleInputDialogEvent: SingleLiveEvent<SimpleInputDialogParam> = SingleLiveEvent()
    //    val stringListDialogLiveEvent: SingleLiveEvent<StringListDialogParam> = SingleLiveEvent()

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