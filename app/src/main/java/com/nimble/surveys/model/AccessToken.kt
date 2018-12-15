package com.nimble.surveys.model

data class AccessToken(
        val access_token: String,
        val created_at: Int,
        val expires_in: Int,
        val token_type: String
)