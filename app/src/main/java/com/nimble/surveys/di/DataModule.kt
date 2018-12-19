package com.nimble.surveys.di

import androidx.room.Room
import com.nimble.surveys.repository.SurveysDb
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val dataModule = module {

    single { Room.databaseBuilder(androidApplication(), SurveysDb::class.java, "surveys-db").build() }

    single { get<SurveysDb>().surveyDao() }

}