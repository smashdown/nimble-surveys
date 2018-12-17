package com.nimble.surveys.ui.detail

import android.content.Intent
import com.nimble.surveys.R
import com.nimble.surveys.base.BaseViewModel
import com.nimble.surveys.model.Survey
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.utils.arch.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DetailViewModel(
    private val surveyDao: SurveyDao
) : BaseViewModel() {

    val title = SingleLiveEvent<String>()
    val survey = SingleLiveEvent<Survey>()

    override fun initData(intent: Intent?): Boolean {
        return true
    }

    override fun initViews(intent: Intent?): Boolean {
        val surveyId = intent?.getStringExtra("id")

        Timber.d("surveyId=%s", surveyId)
        surveyId?.let {
            surveyDao.findById(surveyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onSurveyFetched(result) },
                    { error -> onSurveyFetchFailed(error) }
                )
            return true
        }
        return false
    }

    private fun onSurveyFetched(surveyFetched: Survey) {
        Timber.d("onSurveyFetched() surveyFetched=%s", surveyFetched.id)
        survey.value = surveyFetched
        title.value = surveyFetched.title
    }

    private fun onSurveyFetchFailed(error: Throwable) {
        Timber.e(error)
        toastLiveEvent.value = R.string.cannot_connect
        finishEvent.value = null
    }
}
