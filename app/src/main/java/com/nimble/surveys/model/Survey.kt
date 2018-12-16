package com.nimble.surveys.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "surveys")
data class Survey(
        @PrimaryKey
        var id: String = "",

        var title: String = "",

        var description: String = "",

        // var "access_code_prompt": null,
        // var "thank_email_above_threshold": "<span style=\"font-family:arial,helvetica,sans-serif\"><span style=\"font-size:14px\">Dear {name},<br /><br />Thank you for visiting Scarlett Wine Bar &amp; Restaurant at Pullman Bangkok Hotel G &nbsp;and for taking the time to complete our guest feedback survey!<br /><br />Your feedback is very important to us and each survey is read individually by the management and owners shortly after it is sent. We discuss comments and suggestions at our daily meetings and use them to constantly improve our services.<br /><br />We would very much appreciate it if you could take a few more moments and review us on TripAdvisor regarding your recent visit. By <a href=\"https://www.tripadvisor.com/Restaurant_Review-g293916-d2629404-Reviews-Scarlett_Wine_Bar_Restaurant-Bangkok.html\">clicking here</a> you will be directed to our page.&nbsp;<br /><br />Thank you once again and we look forward to seeing you soon!<br /><br />The Team at Scarlett Wine Bar &amp; Restaurant&nbsp;</span></span><span style=\"font-family:arial,helvetica,sans-serif; font-size:14px\">Pullman Bangkok Hotel G</span>",
        // var "thank_email_below_threshold": "<span style=\"font-size:14px\"><span style=\"font-family:arial,helvetica,sans-serif\">Dear {name},<br /><br />Thank you for visiting&nbsp;</span></span><span style=\"font-family:arial,helvetica,sans-serif; font-size:14px\">Uno Mas at Centara Central World&nbsp;</span><span style=\"font-size:14px\"><span style=\"font-family:arial,helvetica,sans-serif\">&nbsp;and for taking the time to complete our customer&nbsp;feedback survey.</span></span><br /><br /><span style=\"font-family:arial,helvetica,sans-serif; font-size:14px\">The Team at&nbsp;</span><span style=\"font-family:arial,helvetica,sans-serif\"><span style=\"font-size:14px\">Scarlett Wine Bar &amp; Restaurant&nbsp;</span></span><span style=\"font-family:arial,helvetica,sans-serif; font-size:14px\">Pullman Bangkok Hotel G</span>",
        @ColumnInfo(name = "footer_content")
        @Json(name = "footer_content")
        var footerContent: String = "", // "<span style=\"font-family:arial,helvetica,sans-serif\">Scarlett Wine Bar &amp; Restaurant</span><div><span style=\"font-family:arial,helvetica,sans-serif\">Pullman Bangkok Hotel G,&nbsp;</span></div><div><span style=\"font-family:arial,helvetica,sans-serif\">37th floor, 188 Silom Road, Bangrak, Bangkok, Thailand<br />To:&nbsp;096 860 7990,&nbsp;<span style=\"color:rgb(34, 34, 34)\">02 352 4000</span><br />W: <a href=\"http://www.randblab.com/scarlett.html#1\">scarlett</a></span></div><br /><br />&nbsp;",

        @ColumnInfo(name = "is_active")
        @Json(name = "is_active")
        var isActive: Boolean = false,

        @ColumnInfo(name = "cover_image_url")
        @Json(name = "cover_image_url")
        var coverImageUrl: String = "" // "https://dhdbhh0jsld0o.cloudfront.net/m/1ea51560991bcb7d00d0_",

        // var "cover_background_color": null,
        // var "type": "Restaurant",
        // var "created_at": "2017-01-23T14:48:12.991+07:00",
        // var "active_at": "2015-10-08T14:04:00.000+07:00",
        // var "inactive_at": null,
        // var "survey_version": 0,
        // var "short_url": "Scarlettbkk",
        // var "language_list": [ "en" ],
        // var "default_language": "en",
        // var "tag_list": "Scarlett",
        // var "is_access_code_required": false,
        // var "is_access_code_valid_required": false,
        // var "access_code_validation": "",
        // var "theme": { "color_active": "#9B2828","color_inactive": "#000000","color_question": "#ffffff","color_answer_normal": "#FFFFFF","color_answer_inactive": "#FFFFFF" },
        // var "questions":
)