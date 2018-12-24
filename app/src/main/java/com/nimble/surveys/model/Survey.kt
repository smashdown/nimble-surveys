package com.nimble.surveys.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "surveys")
data class Survey(
        @PrimaryKey
        var id: String = "",

        var title: String = "",

        var description: String = "",

        @ColumnInfo(name = "footer_content")
        @Json(name = "footer_content")
        var footerContent: String = "",

        @ColumnInfo(name = "is_active")
        @Json(name = "is_active")
        var isActive: Boolean = false,

        @ColumnInfo(name = "cover_image_url")
        @Json(name = "cover_image_url")
        var coverImageUrl: String = "",

        @ColumnInfo(name = "created_at")
        @Json(name = "created_at")
        var createdAt: Date = Date(),
        @ColumnInfo(name = "active_at")
        @Json(name = "active_at")
        var activeAt: Date = Date()
)