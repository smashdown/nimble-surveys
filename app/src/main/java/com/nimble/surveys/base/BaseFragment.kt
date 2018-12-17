package com.nimble.surveys.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nimble.surveys.BR
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseFragment<DB : ViewDataBinding, out VM : BaseViewModel>(clazz: KClass<VM>) : Fragment() {
    val viewModel: VM by viewModelByClass(clazz) { parametersOf(this) }
    lateinit var binding: DB

    abstract fun getLayoutRes(): Int

    protected open fun initViews(bundle: Bundle?) {
        // DO NOTHING
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        binding.setLifecycleOwner(this)
        binding.setVariable(BR.vm, viewModel)

        // initViews in fragment layer(from the limitation of data binding, sometimes we need to init view in view)
        initViews(savedInstanceState ?: arguments)

        viewModel.initData(activity?.intent)
        viewModel.initData(savedInstanceState ?: arguments)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // initViews in ViewModel layer
        viewModel.initViews(activity?.intent)
        viewModel.initViews(savedInstanceState ?: arguments)

        observeViewModel()
    }

    open fun observeViewModel() {
        viewModel.toastLiveEvent
            .observe(this, Observer { s -> activity?.toast(s.toInt()) })

        viewModel.toastStringLiveEvent
            .observe(this, Observer { string -> activity?.toast(string) })

        viewModel.finishEvent
            .observe(this, Observer { intent ->
                if (intent != null) {
                    activity!!.setResult(Activity.RESULT_OK, intent)
                } else {
                    activity!!.setResult(Activity.RESULT_CANCELED, intent)
                }
                activity!!.finish()
            })
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