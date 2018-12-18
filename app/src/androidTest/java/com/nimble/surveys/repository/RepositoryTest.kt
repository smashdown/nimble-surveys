package com.nimble.surveys.repository

import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nimble.surveys.model.Survey
import com.nimble.surveys.ui.main.MainActivity
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private var foodDao: SurveyDao? = null

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java).onActivity { activity ->
            foodDao = Room.databaseBuilder(
                activity,
                SurveysDb::class.java,
                "surveys-db-test"
            ).build().surveyDao()
        }
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testSaveAllAndFindById() {
        val survey = Survey("ID", "TITLE")
        val surveys: MutableList<Survey> = mutableListOf()
        surveys.add(survey)

        foodDao?.saveAll(surveys)
        foodDao?.findById("ID")!!.test().assertValue(survey)
    }

    @Test
    fun testDeleteAll() {
        val survey = Survey("ID", "TITLE")
        val surveys: MutableList<Survey> = mutableListOf()
        surveys.add(survey)

        foodDao?.saveAll(surveys)
        foodDao?.deleteAll()
        foodDao?.findById("ID")!!.test().assertValueCount(0)
    }

    @Test
    fun testFindAll() {
        foodDao?.deleteAll()
        val survey1 = Survey("ID1", "TITLE", "", "", true, "", Date())
        val survey2 = Survey("ID2", "TITLE", "", "", true, "", Date())
        val survey3 = Survey("ID3", "TITLE", "", "", true, "", Date())

        val surveys: MutableList<Survey> = mutableListOf()
        surveys.addAll(listOf(survey1, survey2, survey3))

        foodDao?.saveAll(surveys)
        foodDao?.findAll()!!.test().assertOf { t -> t.assertEmpty() }
    }

    @Test
    fun testCount() {
        foodDao?.deleteAll()
        val survey1 = Survey("ID1", "TITLE")
        val survey2 = Survey("ID2", "TITLE")
        val survey3 = Survey("ID3", "TITLE")

        val surveys: MutableList<Survey> = mutableListOf()
        surveys.addAll(listOf(survey1, survey2, survey3))

        foodDao?.saveAll(surveys)

        Assert.assertThat(foodDao?.count(), `is`(3))
    }
}