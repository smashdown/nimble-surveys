package com.nimble.surveys.viewmodel


import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nimble.surveys.utils.RxImmediateSchedulerRule
import com.nimble.surveys.model.Survey
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.viewmodel.detail.DetailViewModel
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

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
        MockitoAnnotations.initMocks(this)
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
