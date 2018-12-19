package com.nimble.surveys.di

import com.nimble.surveys.viewmodel.base.DummyViewModel
import com.nimble.surveys.viewmodel.detail.DetailViewModel
import com.nimble.surveys.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModules = module {
    viewModel { DummyViewModel() }
    viewModel { MainViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
}
