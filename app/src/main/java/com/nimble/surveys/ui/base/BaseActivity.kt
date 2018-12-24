package com.nimble.surveys.ui.base

import android.app.Activity
import android.os.Bundle
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

        // to support default transition animation for activity
        when (getTransitionAnimationDirection()) {
            HSTransitionDirection.FROM_RIGHT_TO_LEFT -> overridePendingTransition(
                R.anim.pull_in_right,
                R.anim.push_out_left
            )
            HSTransitionDirection.FROM_LEFT_TO_RIGHT -> overridePendingTransition(
                R.anim.pull_in_left,
                R.anim.push_out_right
            )
            HSTransitionDirection.FROM_TOP_TO_BOTTOM -> overridePendingTransition(
                R.anim.pull_in_top,
                R.anim.push_out_bottom
            )
            HSTransitionDirection.FROM_BOTTOM_TO_TOP -> overridePendingTransition(R.anim.pull_in_bottom, R.anim.stay)
            else -> {
                // DO NOTHING FOR NONE
            }
        }

        binding = DataBindingUtil.setContentView(this, getLayoutRes())
        binding.setLifecycleOwner(this)
        binding.setVariable(BR.vm, viewModel)

        initViews()
        observeViewModel()
    }

    open fun observeViewModel() {
        viewModel.toastLiveEvent
            .observe(this, Observer { stringResId -> toast(stringResId.toInt()) })
        viewModel.toastStringLiveEvent
            .observe(this, Observer { string -> toast(string) })

        viewModel.finishEvent
            .observe(this, Observer { intent ->
                if (intent != null) setResult(Activity.RESULT_OK, intent) else setResult(
                    Activity.RESULT_CANCELED,
                    intent
                )
                finish()
            })
    }

    // Up button default behavior
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun finish() {
        super.finish()

        // to support default transition animation for activity
        when (getTransitionAnimationDirection()) {
            HSTransitionDirection.FROM_RIGHT_TO_LEFT -> overridePendingTransition(
                R.anim.pull_in_left,
                R.anim.push_out_right
            )
            HSTransitionDirection.FROM_LEFT_TO_RIGHT -> overridePendingTransition(
                R.anim.pull_in_right,
                R.anim.push_out_left
            )
            HSTransitionDirection.FROM_TOP_TO_BOTTOM -> overridePendingTransition(
                R.anim.pull_in_bottom,
                R.anim.push_out_top
            )
            HSTransitionDirection.FROM_BOTTOM_TO_TOP -> overridePendingTransition(R.anim.stay, R.anim.push_out_bottom)
            else -> {
                // DO NOTHING for NONE
            }
        }
    }
}