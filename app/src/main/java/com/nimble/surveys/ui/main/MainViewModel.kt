package com.nimble.surveys.ui.main

import android.content.Intent
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
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

    var canLoadMore = true

    init {
        this.adapter.setHasStableIds(true)
    }

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

                            indicatorCountChanged.value = items.size
                        }
        )
    }

    override fun onRefresh() {
        Timber.d("onRefresh()")
        if (status.value == Status.LOADING) {
            Timber.d("Already loading")
            return
        }
        status.value = Status.LOADING

        disposables.add(
                surveysApi.auth()
                        .concatMap { accessToken -> surveysApi.getSurveyList(accessToken.access_token, 0) }
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
        disposables.add(
                Observable.fromCallable {
                    if (clear)
                        surveyDao.deleteAll()
                    surveyDao.saveAll(surveyList)
                    status.postValue(if (surveyDao.count() > 0) Status.LOADED else Status.EMPTY)
                }.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe({
                            Timber.d("Inserted ${surveyList.size} surveys from API in DB...")
                            canLoadMore = surveyList.isNotEmpty()
                        }, { error ->
                            Timber.e(error)
                        })
        )
    }

    private fun onSurveyFailed(error: Throwable) {
        Timber.e(error, error.localizedMessage)
        canLoadMore = false
        disposables.add(
                Observable.fromCallable {
                    if (surveyDao.count() < 1) {
                        status.postValue(Status.FAILED)
                        toastLiveEvent.postValue(error.localizedMessage)
                    }
                }.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(
                                { Timber.d("API calling has failed.") },
                                { error -> Timber.e(error) }
                        )
        )

    }

    fun onClickSurvey(view: ImageView, item: Survey) {
        Timber.d("onClickSurvey() - surveyId=%s", item.id)
        // navDetail.value = item

        val intent = Intent(fragment.activity!!, DetailActivity::class.java)
        intent.putExtra("id", item.id)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(fragment.activity!!, view, "coverImage")
        ContextCompat.startActivity(fragment.activity!!, intent, options.toBundle())
    }

    override fun canLoadMore(): Boolean {
        return canLoadMore
    }

    override fun onLoadMore() {
        Timber.d("loadMore()")

        if (canLoadMore) {
            disposables.add(
                    surveysApi.auth()
                            .concatMap { accessToken -> surveysApi.getSurveyList(accessToken.access_token, items.size / BuildConfig.PAGE_UNIT) }
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
