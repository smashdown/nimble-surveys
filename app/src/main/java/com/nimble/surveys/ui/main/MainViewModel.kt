package com.nimble.surveys.ui.main

import androidx.fragment.app.Fragment
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.base.BaseViewModel
import com.nimble.surveys.model.Survey
import com.nimble.surveys.model.common.Status
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.utils.arch.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(
    private val fragment: Fragment,
    private val surveysApi: SurveysApi,
    private val surveyDao: SurveyDao
) : BaseViewModel() {
    val status: SingleLiveEvent<Status> = SingleLiveEvent(Status.EMPTY)

    fun loadSurveys() {
        disposables.add(
            surveyDao.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { surveys ->
                    Timber.d("article size=%s", surveys.size)

                    status.value = if (surveys.isEmpty()) Status.EMPTY else Status.LOADED

                    //                    items.clear()
                    //                    items.addAll(articles)
                    //                    articlesAdapter.notifyDataSetChanged()
                }
        )
    }

    fun onRefresh() {
        Timber.d("onRefresh()")
        if (status.value == Status.LOADING) {
            Timber.d("Already loading")
            return
        }

        status.value = Status.LOADING

        disposables.add(
            surveysApi.auth()
                .concatMap { accessToken -> surveysApi.articles(accessToken.access_token) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onSurveyFetched(result) },
                    { error -> onSurveyFailed(error) }
                )
        )
    }

    fun onClickMenu() {

    }

    private fun onSurveyFetched(surveyList: List<Survey>) {
        disposables.add(
            Observable.fromCallable {
                surveyDao.deleteAll()
                surveyDao.saveAll(surveyList)
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { Timber.d("Inserted ${surveyList.size} surveys from API in DB...") },
                    { error -> Timber.e(error) }
                )
        )
    }

    private fun onSurveyFailed(error: Throwable) {
        Timber.e(error, error.localizedMessage)
    }
}
