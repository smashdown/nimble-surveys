package com.nimble.surveys

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.di.appModules
import com.nimble.surveys.di.dataModule
import com.nimble.surveys.di.networkModule
import com.nimble.surveys.model.AccessToken
import com.nimble.surveys.model.Survey
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.ui.main.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
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

class MainViewModelTest : KoinTest {
    inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private var context: Application = mock(Application::class.java)
    private val viewModel: MainViewModel by inject()

    @Before
    fun setupTasksViewModel() {
        startKoin(appModules + dataModule + networkModule) with (context)

        MockitoAnnotations.initMocks(this)

        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock())

        // To support Rx test
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testOnClickMenu() {
        with(viewModel) {
            val observer: Observer<Int> = mock()
            viewModel.toastLiveEvent.observeForever(observer)

            onClickMenu()

            verify(observer, times(1)).onChanged(any())
        }
    }

    @Test
    fun testOnClickSurvey() {
        with(viewModel) {
            val view: ImageView = mock()
            val survey: Survey = mock()
            survey.id = "temp"

            val observer: Observer<View> = mock()
            viewModel.navMain.observeForever(observer)

            onClickSurvey(view, survey)

            verify(observer, times(1)).onChanged(any())
        }
    }

    @Test
    fun testOnRefresh() {
        val surveysApi: SurveysApi = mock()
        val surveyDao: SurveyDao = mock()
        val mainViewModel = MainViewModel(surveysApi, surveyDao)
        `when`(surveysApi.auth()).thenReturn(Observable.just(AccessToken("123", 123, 123, "bearer")))
        `when`(surveysApi.getSurveyList(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(listOf()))

        mainViewModel.onRefresh()

        verify(surveyDao, times(1)).deleteAll()
    }

    @Test
    fun testOnLoadMore() {
        val surveysApi: SurveysApi = mock()
        val surveyDao: SurveyDao = mock()
        val mainViewModel = MainViewModel(surveysApi, surveyDao)
        `when`(surveysApi.auth()).thenReturn(Observable.just(AccessToken("123", 123, 123, "bearer")))
        `when`(surveysApi.getSurveyList(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(listOf()))

        mainViewModel.onLoadMore()

        verify(surveyDao, never()).deleteAll()
    }

}
