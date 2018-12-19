package com.nimble.surveys.model

import com.squareup.moshi.Json

data class AccessToken(
    @Json(name = "accessToken")
    val accessToken: String,
    @Json(name = "createdAt")
    val createdAt: Int,
    @Json(name = "expiresIn")
    val expiresIn: Int,
    @Json(name = "tokenType")
    val tokenType: String
)