package com.nimble.surveys


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
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.concurrent.thread

class DetailViewModelTest : KoinTest {
    inline fun <reified T : Any> mock() = Mockito.mock(T::class.java)

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Before
    fun setupTasksViewModel() {
        startKoin(appModules + dataModule + networkModule)

        MockitoAnnotations.initMocks(this)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testInitViews() {
        val intent: Intent = mock()
        val surveyDao: SurveyDao = mock()
        val detailViewModel = DetailViewModel(surveyDao)

        val observer: Observer<String> = mock()
        detailViewModel.title.observeForever(observer)

        `when`(intent.getStringExtra(anyString())).thenReturn("123")
        `when`(surveyDao.findById(anyString())).thenReturn(Single.just(Survey("id", "title")))

        detailViewModel.initViews(intent)

        verify(observer, times(1)).onChanged(anyString())
    }
}
