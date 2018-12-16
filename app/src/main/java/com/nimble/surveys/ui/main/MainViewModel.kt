package com.nimble.surveys.ui.main

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.BuildConfig
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.base.BaseViewModel
import com.nimble.surveys.model.Survey
import com.nimble.surveys.model.common.Status
import com.nimble.surveys.repository.SurveyDao
import com.nimble.surveys.ui.detail.DetailActivity
import com.nimble.surveys.ui.main.adapter.SurveyListAdapter
import com.nimble.surveys.utils.arch.SingleLiveEvent
import com.nimble.surveys.utils.views.LoadMoreListener
import com.nimble.surveys.utils.views.OnScrollLoadMoreListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.startActivity
import timber.log.Timber

class MainViewModel(
        private val fragment: Fragment,
        private val surveysApi: SurveysApi,
        private val surveyDao: SurveyDao
) : BaseViewModel(), LoadMoreListener {

    val items: MutableList<Survey> = mutableListOf()
    val status: SingleLiveEvent<Status> = SingleLiveEvent(Status.EMPTY)
    val adapter: SurveyListAdapter = SurveyListAdapter(this, items)
    val onScrollLoadMoreListener = OnScrollLoadMoreListener(this, false)
    val layoutManager: RecyclerView.LayoutManager
        get() = LinearLayoutManager(fragment.context)
    val indicatorCountChanged: SingleLiveEvent<Int> = SingleLiveEvent(0)

    var page = 0
    var canLoadMore = true

    fun loadSurveys() {
        disposables.add(
                surveyDao.findAll()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { surveys ->
                            Timber.d("survey count=%s", surveys.size)

                            items.clear()
                            items.addAll(surveys)
                            adapter.notifyDataSetChanged()

                            indicatorCountChanged.value = surveys.size

                            items.forEach { item -> Timber.d("items url=%s", item.coverImageUrl) }
                        }
        )
    }

    override fun onRefresh() {
        Timber.d("onRefresh()")
        if (status.value == Status.LOADING) {
            Timber.d("Already loading")
            return
        }
        page = 0
        status.value = Status.LOADING

        disposables.add(
                surveysApi.auth()
                        .concatMap { accessToken -> surveysApi.getSurveyList(accessToken.access_token, page) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { result -> onSurveyFetched(result, true) },
                                { error -> onSurveyFailed(error) }
                        )
        )
    }

    fun onClickMenu() {
        toastLiveEvent.value = "Menu button clicked"
    }

    private fun onSurveyFetched(surveyList: List<Survey>, clear: Boolean) {
        canLoadMore = surveyList.size >= BuildConfig.PAGE_UNIT

        disposables.add(
                Observable.fromCallable {
                    if (clear)
                        surveyDao.deleteAll()
                    surveyDao.saveAll(surveyList)
                    status.postValue(if (surveyDao.count() < 1) Status.EMPTY else Status.LOADED)
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
        status.value = Status.FAILED
        toastLiveEvent.value = error.localizedMessage
    }

    fun onClickSurvey(item: Survey) {
        Timber.d("onClickSurvey() - surveyId=%s", item.id)
        fragment.activity?.startActivity<DetailActivity>("id" to item.id)
    }

    override fun canLoadMore(): Boolean {
        return canLoadMore
    }

    override fun onLoadMore() {
        Timber.d("loadMore()")

        if (canLoadMore) {
            page += 1
            disposables.add(
                    surveysApi.auth()
                            .concatMap { accessToken -> surveysApi.getSurveyList(accessToken.access_token, page) }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> onSurveyFetched(result, false) },
                                    { error -> onSurveyFailed(error) }
                            )
            )
        }
    }
}
