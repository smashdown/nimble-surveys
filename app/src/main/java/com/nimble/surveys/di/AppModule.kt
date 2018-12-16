package com.nimble.surveys.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.nimble.surveys.base.DummyViewModel
import com.nimble.surveys.ui.detail.DetailViewModel
import com.nimble.surveys.ui.main.MainViewModel
import com.nimble.surveys.utils.rx.ApplicationSchedulerProvider
import com.nimble.surveys.utils.rx.SchedulerProvider
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import java.util.*

val mvvmModules = module {

    viewModel { (activity: Activity) -> DummyViewModel() }
    viewModel { (fragment: Fragment) -> MainViewModel(fragment, get(), get()) }
    viewModel { (fragment: Fragment) -> DetailViewModel(fragment, get(), get()) }

    single { createMoshi() }
}

fun createMoshi(): Moshi {
    return Moshi
            .Builder()
            .add(NULL_TO_EMPTY_STRING_ADAPTER)
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
}

val rxModule = module {
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}

// Gather all app modules
val appModules = listOf(mvvmModules, rxModule)

object NULL_TO_EMPTY_STRING_ADAPTER {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()
        return ""
    }
}