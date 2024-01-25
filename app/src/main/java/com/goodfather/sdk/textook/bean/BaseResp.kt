package com.goodfather.sdk.textook.bean

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResp<T> (
    val data: T,
    val message: String,
    val code: Int
){

}