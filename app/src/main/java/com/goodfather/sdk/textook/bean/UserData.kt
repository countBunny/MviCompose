package com.goodfather.sdk.textook.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserData(
    val accessToken: String,
    val userId: Int,
    val tokenExpireTime: String,
    val deviceId: String
) {
}