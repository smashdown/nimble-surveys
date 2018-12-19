package com.nimble.surveys.api

import com.nimble.surveys.BuildConfig
import com.nimble.surveys.model.AccessToken
import com.nimble.surveys.model.Survey
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

@JvmSuppressWildcards
interface SurveysApi {

    @POST("oauth/token")
    fun auth(
        @Query("grant_type") grantType: String = BuildConfig.OAUTH_GRANT_TYPE,
        @Query("username") username: String = BuildConfig.OAUTH_USERNAME,
        @Query("password") password: String = BuildConfig.OAUTH_PASSWORD
    ): Observable<AccessToken>

    @GET("surveys.json")
    fun getSurveyList(
        @Query("access_token") accessToken: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = BuildConfig.PAGE_UNIT
    ): Observable<List<Survey>>

}
