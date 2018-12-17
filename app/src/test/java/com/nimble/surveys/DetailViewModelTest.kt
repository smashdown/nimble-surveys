package com.nimble.surveys


import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nimble.surveys.di.appModules
import com.nimble.surveys.di.dataModule
import com.nimble.surveys.di.networkModule
import com.nimble.surveys.model.Survey
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.ui.detail.DetailViewModel
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.with
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

class DetailViewModelTest : KoinTest {
    inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val rule = MockitoJUnit.rule()!!

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private var context: Application = mock(Application::class.java)
    private val viewModel: DetailViewModel by inject()

    @Before
    fun setupTasksViewModel() {
        startKoin(appModules + dataModule + networkModule) with (context)

        MockitoAnnotations.initMocks(this)

        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock())
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testInitViews() {
        val intent: Intent = mock()
        intent.putExtra("id", "test")

        val surveyDao: SurveyDao = mock()
        val detailViewModel = DetailViewModel(surveyDao)

        val observer: Observer<Survey> = mock()
        detailViewModel.survey.observeForever(observer)

        `when`(surveyDao.findById(anyString())).thenReturn(Single.just(Survey("id", "title")))

        detailViewModel.initViews(intent)

        verify(observer, times(1)).onChanged(any())
    }
}
