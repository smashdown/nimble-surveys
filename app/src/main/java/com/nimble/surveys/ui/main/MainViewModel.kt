package com.nimble.surveys.ui.main

import android.view.View
import android.widget.ImageView
import com.nimble.surveys.BuildConfig
import com.nimble.surveys.R
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.base.BaseViewModel
import com.nimble.surveys.model.Survey
import com.nimble.surveys.model.common.Status
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.ui.main.adapter.SurveyPagerAdapter
import com.nimble.surveys.utils.arch.SingleLiveEvent
import com.nimble.surveys.utils.views.OnPageSelected
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(
    var surveysApi: SurveysApi,
    private val surveyDao: SurveyDao
) : BaseViewModel(), OnPageSelected {

    val status = SingleLiveEvent(Status.EMPTY)

    var adapter: SurveyPagerAdapter = SurveyPagerAdapter(this)

    val navMain = SingleLiveEvent<View>() // survey ID included in tag

    fun loadSurveys() {
        launch {
            surveyDao.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { surveys ->
                    Timber.d("survey count=%s", surveys.size)

                    adapter.items.clear()
                    adapter.items.addAll(surveys)
                    adapter.notifyDataSetChanged()
                }
        }
    }

    override fun onPageSelected(position: Int) {
        Timber.d("onPageSelected() position=%s", position.toString())

        if (adapter.items.size != position + 1) {
            // Load more only when user reach to final page
            return
        }

        if (status.value == Status.LOADING) {
            Timber.d("Already loading")
            return
        }

        status.value = Status.LOADING

        launch {
            surveysApi.auth()
                .concatMap { accessToken ->
                    surveysApi.getSurveyList(
                        accessToken.access_token,
                        adapter.items.size / BuildConfig.PAGE_UNIT
                    )
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onSurveyFetched(result, false) },
                    { error -> onSurveyFailed(error) }
                )
        }
    }

    fun onRefresh() {
        Timber.d("onRefresh()")
        if (status.value == Status.LOADING) {
            Timber.d("Already loading")
            return
        }
        status.value = Status.LOADING

        launch {
            surveysApi.auth()
                .concatMap { accessToken -> surveysApi.getSurveyList(accessToken.access_token, 0) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> onSurveyFetched(result, true) },
                    { error -> onSurveyFailed(error) }
                )
        }
    }

    private fun onSurveyFetched(surveyList: List<Survey>, clear: Boolean) {
        launch {
            Observable.fromCallable {
                if (clear)
                    surveyDao.deleteAll()
                surveyDao.saveAll(surveyList)
                status.postValue(if (surveyDao.count() > 0) Status.LOADED else Status.EMPTY)
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    Timber.d("Inserted ${surveyList.size} surveys from API in DB...")
                }, { error ->
                    Timber.e(error)
                })
        }
    }

    private fun onSurveyFailed(error: Throwable) {
        Timber.e(error, error.localizedMessage)

        launch {
            Observable.fromCallable {
                if (surveyDao.count() < 1) {
                    status.postValue(Status.FAILED)
                    toastStringLiveEvent.postValue(error.localizedMessage)
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    { Timber.d("API calling has failed.") },
                    { error -> Timber.e(error) }
                )
        }

    }

    fun onClickSurvey(view: ImageView, item: Survey) {
        Timber.d("onClickSurvey() - surveyId=%s", item.id)

        view.setTag(R.id.ivBackground, item.id)
        navMain.value = view
    }

    fun onClickMenu() {
        toastLiveEvent.value = R.string.menu_clicked
    }

}
