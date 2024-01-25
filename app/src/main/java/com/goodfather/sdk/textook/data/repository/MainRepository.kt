package com.goodfather.sdk.textook.data.repository

import com.goodfather.sdk.textook.GpapaFacade
import com.goodfather.sdk.textook.network.HeadMgr
import com.goodfather.sdk.textook.network.ISdkService

class MainRepository(private val sdkService: ISdkService) {

    suspend fun login(partnerUserId: String) =
        sdkService.sdkUserLogin(HeadMgr.instance.appId, GpapaFacade.getPackageName(), partnerUserId, HeadMgr.instance.deviceId)

    suspend fun bookDetails(productionId: String) =
        sdkService.sdkBookDetails(productionId)

}