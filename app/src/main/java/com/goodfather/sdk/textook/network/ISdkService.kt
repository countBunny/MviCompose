package com.goodfather.sdk.textook.network

import com.goodfather.sdk.textook.bean.BaseResp
import com.goodfather.sdk.textook.bean.BookDetail
import com.goodfather.sdk.textook.bean.UserData

import retrofit2.http.GET
import retrofit2.http.Query


interface ISdkService {

    @GET("/sdk/user/login")
    suspend fun sdkUserLogin(
        @Query("appId") appId: String, @Query("packageName") packageName: String,
        @Query("partnerUserId") partnerUserId: String,
        @Query("deviceId") deviceId: String?
    ): BaseResp<UserData>

    @GET("/sdk/book/details")
    suspend fun sdkBookDetails(
        @Query("productionId") productionId: String
    ): BaseResp<BookDetail>

}