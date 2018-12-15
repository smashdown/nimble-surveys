package com.nimble.surveys.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.nimble.surveys.BR
import com.nimble.surveys.R
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseActivity<B : ViewDataBinding, out VM : BaseViewModel>(clazz: KClass<VM>) : AppCompatActivity() {
    val viewModel: VM by viewModelByClass(clazz) { parametersOf(this) }
    lateinit var binding: B

    enum class HSTransitionDirection {
        NONE, FROM_RIGHT_TO_LEFT, FROM_LEFT_TO_RIGHT, FROM_TOP_TO_BOTTOM, FROM_BOTTOM_TO_TOP
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun initViews()

    open fun getTransitionAnimationDirection(): HSTransitionDirection {
        return HSTransitionDirection.FROM_RIGHT_TO_LEFT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (getTransitionAnimationDirection()) {
            HSTransitionDirection.FROM_RIGHT_TO_LEFT -> overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
            HSTransitionDirection.FROM_LEFT_TO_RIGHT -> overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
            HSTransitionDirection.FROM_TOP_TO_BOTTOM -> overridePendingTransition(R.anim.pull_in_top, R.anim.push_out_bottom)
            HSTransitionDirection.FROM_BOTTOM_TO_TOP -> overridePendingTransition(R.anim.pull_in_bottom, R.anim.stay)
        }

        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        binding.setLifecycleOwner(this)
        binding.setVariable(BR.vm, viewModel)

        initViews()
        observeViewModel()
    }

    open fun observeViewModel() {
//        viewModel.apiErrorEvent
//                .observe(this, Observer{ throwable ->
//                    if (throwable is HttpException) {
//                        try {
//                            val apiError = errorConverter.convert((throwable as HttpException).response().errorBody())
//                            AppUtil.handleServerError(this, apiError, false)
//                        } catch (e: IOException) {
//                            Timber.e(e, e.getLocalizedMessage())
//                            UiUtil.toast(this, R.string.hs_something_went_wrong)
//                        }
//
//                    } else {
//                        Timber.d(throwable, throwable.getLocalizedMessage())
//                        AppUtil.handleNetworkFailException(this, false)
//                    }
//                })
//
//        viewModel.permissionRequestEvent
//                .observe(this, { permissionRequestParam ->
//                    Dexter.withActivity(this)
//                            .withPermissions(permissionRequestParam.permissions)
//                            .withListener(permissionRequestParam.multiplePermissionsListener)
//                            .check()
//                })

        viewModel.toastLiveEvent
                .observe(this, Observer { stringResId -> toast(stringResId) })

//        viewModel.snackBarLiveEvent
//                .observe(this, object : Observer<String>() {
//                    fun onChanged(@Nullable s: String) {
//                        // FIXME:
//                    }
//                })

//        viewModel.progressDialogLiveEvent
//                .observe(this, Observer { msg ->
//                    if (TextUtils.isEmpty(msg))
//                        progressDialog?.dismiss()
//                    else
//                        progressDialog = progressDialog(message = msg)
//                    if (TextUtils.isEmpty(msg)) {
//                        UiUtil.hideProgressDialog(progressDialog)
//                    } else {
//                        progressDialog = UiUtil.showProgressDialog(this, progressDialog, msg)
//                    }
//                })
//
//        viewModel.confirmDialogLiveEvent
//                .observe(this, { confirmDialogParam -> UiUtil.showConfirmDialog(this, confirmDialogParam) })
//
//        viewModel.simpleInputDialogEvent
//                .observe(this, { simpleInputDialogParam -> UiUtil.showInputTextDialog(this, simpleInputDialogParam) })
//
//        viewModel.stringListDialogLiveEvent
//                .observe(this, { stringListDialogParam -> UiUtil.showStringListDialog(this, stringListDialogParam) })

        viewModel.finishEvent
                .observe(this, Observer { intent ->
                    if (intent != null) setResult(Activity.RESULT_OK, intent) else setResult(Activity.RESULT_CANCELED, intent)
                    finish()
                })

        TextUtils.isEmpty("a")
    }

    // Up button default behavior
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()

        when (getTransitionAnimationDirection()) {
            HSTransitionDirection.FROM_RIGHT_TO_LEFT -> overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right)
            HSTransitionDirection.FROM_LEFT_TO_RIGHT -> overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left)
            HSTransitionDirection.FROM_TOP_TO_BOTTOM -> overridePendingTransition(R.anim.pull_in_bottom, R.anim.push_out_top)
            HSTransitionDirection.FROM_BOTTOM_TO_TOP -> overridePendingTransition(R.anim.stay, R.anim.push_out_bottom)
        }
    }
}