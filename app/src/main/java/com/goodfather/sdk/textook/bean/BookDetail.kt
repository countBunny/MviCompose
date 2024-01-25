package com.goodfather.sdk.textook.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookDetail(
    val subjectId: String,
    val pinyinToneInfo: PinyinToneInfo,
    val payBeginPage: Int,
    val download_try_url: String,
    val download_completed_url: String,
    val recover: Recover,
    val isSelf: Boolean,
    val publishingId: String,
    val bookId: String,
    val productionId: Int
) {
}

@JsonClass(generateAdapter = true)
data class PinyinToneInfo (
    val download_url: String,
    val version_code: String
)

@JsonClass(generateAdapter = true)
data class Recover(
    val purchased: Boolean,
    val validity: String
)