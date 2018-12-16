package com.nimble.surveys.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nimble.surveys.model.Survey

@Database(
        entities = [
            Survey::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SurveysDb : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao
}
