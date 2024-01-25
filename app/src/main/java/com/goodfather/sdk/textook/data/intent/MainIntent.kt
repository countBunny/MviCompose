package com.goodfather.sdk.textook.data.intent

sealed class MainIntent {

    /**
     * 登录
     */
    data class OpenBook(var productionId: Int, var partnerUserId: String): MainIntent()
}