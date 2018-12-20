package com.nimble.surveys.model

import com.squareup.moshi.Json

data class AccessToken(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "created_at")
    val createdAt: Int,
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "token_type")
    val tokenType: String
)