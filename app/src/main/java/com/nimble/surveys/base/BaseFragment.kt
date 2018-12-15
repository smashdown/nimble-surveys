package com.nimble.surveys.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.nimble.surveys.BR
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseFragment<DB : ViewDataBinding, out VM : BaseViewModel>(clazz: KClass<VM>) : Fragment() {
    public val viewModel: VM by viewModelByClass(clazz) { parametersOf(this) }
    protected lateinit var binding: DB

    abstract fun getLayoutRes(): Int

    protected open fun initViews(bundle: Bundle?) {
        // DO NOTHING
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("onCreateView()")

        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        binding.setLifecycleOwner(this)
        binding.setVariable(BR.vm, viewModel)

        viewModel.initData(activity?.intent)
        viewModel.initData(savedInstanceState ?: arguments)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated()")

        // initViews in fragment layer
        initViews(savedInstanceState ?: arguments)

        // initViews in ViewModel layer
        viewModel.initViews(activity?.intent)
        viewModel.initViews(savedInstanceState ?: arguments)

        observeViewModel()
    }

    open fun observeViewModel() {
        // TODO
//        viewModel.apiErrorEvent
//                .observe(this, { throwable ->
//                    if (throwable is HttpException) {
//                        try {
//                            val apiError = errorConverter.convert((throwable as HttpException).response().errorBody())
//                            AppUtil.handleServerError(activity, apiError, false)
//                        } catch (e: IOException) {
//                            Timber.e(e, e.getLocalizedMessage())
//                            UiUtil.toast(activity, R.string.hs_something_went_wrong)
//                        }
//
//                    } else {
//                        AppUtil.handleNetworkFailException(activity, false)
//                    }
//                })
//
//        viewModel.permissionRequestEvent
//                .observe(this, { permissionRequestParam -> Dexter.withActivity(activity).withPermissions(permissionRequestParam.permissions).withListener(permissionRequestParam.multiplePermissionsListener).check() })
//
//        viewModel.toastLiveEvent
//                .observe(this, { s -> UiUtil.toast(activity, s) })
//
//        viewModel.snackBarLiveEvent
//                .observe(this, object : Observer<String>() {
//                    fun onChanged(s: String?) {
//                        // FIXME:
//                    }
//                })
//
//        viewModel.progressDialogLiveEvent
//                .observe(this, { msg ->
//                    if (TextUtils.isEmpty(msg)) {
//                        UiUtil.hideProgressDialog(progressDialog)
//                    } else {
//                        progressDialog = UiUtil.showProgressDialog(activity, progressDialog, msg)
//                    }
//                })
//
//        viewModel.confirmDialogLiveEvent
//                .observe(this, { confirmDialogParam -> UiUtil.showConfirmDialog(activity, confirmDialogParam) })
//
//        viewModel.simpleInputDialogEvent
//                .observe(this, { simpleInputDialogParam -> UiUtil.showInputTextDialog(activity, simpleInputDialogParam) })
//
//        viewModel.stringListDialogLiveEvent
//                .observe(this, { stringListDialogParam -> UiUtil.showStringListDialog(activity, stringListDialogParam) })
//
//        viewModel.finishEvent
//                .observe(this, { intent ->
//                    if (intent != null) {
//                        activity!!.setResult(Activity.RESULT_OK, intent)
//                    } else {
//                        activity!!.setResult(Activity.RESULT_CANCELED, intent)
//                    }
//                    activity!!.finish()
//                })
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceState(outState)
    }

    override fun onViewStateRestored(@Nullable savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.restoreInstanceState(savedInstanceState)
    }
}