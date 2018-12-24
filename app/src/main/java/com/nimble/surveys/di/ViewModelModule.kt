package com.nimble.surveys.di

import com.nimble.surveys.ui.base.DummyViewModel
import com.nimble.surveys.ui.detail.DetailViewModel
import com.nimble.surveys.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModules = module {
    viewModel { DummyViewModel() }
    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
}
