package com.nimble.surveys.di

import com.nimble.surveys.utils.rx.ApplicationSchedulerProvider
import com.nimble.surveys.utils.rx.SchedulerProvider
import org.koin.dsl.module.module

val rxModule = module {
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}
