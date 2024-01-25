package com.goodfather.sdk.textook.data.state

import com.goodfather.sdk.textook.bean.BookDetail
import com.goodfather.sdk.textook.bean.SdkError

sealed class MainState {

    /**
     * 加载中
     */
    object Loading: MainState()

    object Idle: MainState()

    data class BookDetailResp(val detail: BookDetail): MainState()

    data class Error(val sdkError: SdkError): MainState()
}