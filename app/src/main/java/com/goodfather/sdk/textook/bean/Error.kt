package com.goodfather.sdk.textook.bean

sealed class SdkError(val code: Int, message: String): Error(message) {
    object LoginError: SdkError(1, "登录异常")
}