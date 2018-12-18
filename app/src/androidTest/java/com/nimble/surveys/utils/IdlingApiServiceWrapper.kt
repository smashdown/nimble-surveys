package com.nimble.surveys.utils

import android.os.Handler
import android.os.Looper
import androidx.test.espresso.IdlingResource
import com.nimble.surveys.api.SurveysApi
import com.nimble.surveys.model.AccessToken
import com.nimble.surveys.model.Survey
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.atomic.AtomicInteger


internal class IdlingApiServiceWrapper(private val api: SurveysApi) : SurveysApi, IdlingResource {
    private val counter: AtomicInteger
    private val callbacks: MutableList<IdlingResource.ResourceCallback>

    init {
        this.callbacks = ArrayList()
        this.counter = AtomicInteger(0)
    }

    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return counter.get() === 0
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        callbacks.add(resourceCallback)
    }

    private fun notifyIdle() {
        if (counter.get() === 0) {
            for (cb in callbacks) {
                cb.onTransitionToIdle()
            }
        }
    }

    override fun auth(grantType: String, username: String, password: String): Observable<AccessToken> {
        counter.incrementAndGet()
        return api.auth().flatMap { accessToken ->
            counter.decrementAndGet()
            ObservableSource<AccessToken> { accessToken }
        }
    }

    override fun getSurveyList(accessToken: String, page: Int, perPage: Int): Observable<List<Survey>> {
        counter.incrementAndGet()
        return api.getSurveyList(accessToken, page, perPage).flatMap { list ->
            counter.decrementAndGet()
            ObservableSource<List<Survey>> { list }
        }
    }
}