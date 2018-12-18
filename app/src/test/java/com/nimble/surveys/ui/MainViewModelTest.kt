package com.nimble.surveys.ui

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.model.AccessToken
import com.nimble.surveys.model.Survey
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.ui.main.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MainViewModelTest {
    inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var surveysApi: SurveysApi
    private lateinit var surveyDao: SurveyDao
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setupTasksViewModel() {
        MockitoAnnotations.initMocks(this)

        surveysApi = mock()
        surveyDao = mock()
        mainViewModel = MainViewModel(surveysApi, surveyDao)

        // To support Rx test
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testOnClickMenu() {
        with(mainViewModel) {
            val observer: Observer<Int> = mock()
            mainViewModel.toastLiveEvent.observeForever(observer)

            onClickMenu()

            verify(observer, times(1)).onChanged(any())
        }
    }

    @Test
    fun testOnClickSurvey() {
        with(mainViewModel) {
            val view: ImageView = mock()
            val survey: Survey = mock()
            survey.id = "temp"

            val observer: Observer<View> = mock()
            mainViewModel.navMain.observeForever(observer)

            onClickSurvey(view, survey)

            verify(observer, times(1)).onChanged(any())
        }
    }

    @Test
    fun testOnRefresh() {
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
