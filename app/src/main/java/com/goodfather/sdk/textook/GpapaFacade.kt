package com.goodfather.sdk.textook

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import androidx.core.text.TextUtilsCompat
import com.github.gzuliyujiang.oaid.DeviceID
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.github.gzuliyujiang.oaid.IGetter
import com.goodfather.sdk.textook.network.HeadMgr
import com.goodfather.sdk.textook.utils.Constant
import com.goodfather.sdk.textook.utils.EasyDataStore
import java.lang.Exception
import java.util.UUID


class GpapaFacade: Application {

    private constructor() {}

    companion object  {
        const val TAG = "goodfather_log"
        lateinit var ctx: Application
        var mPrepared = false

        @JvmStatic
        fun init(application: Application) {
            ctx = application
            val oaid = EasyDataStore.getData(Constant.DS_OAID, "")
            if (!TextUtils.isEmpty(oaid)) {
                HeadMgr.instance.apply {
                    deviceId = oaid
                }
                mPrepared = true

            } else {
                DeviceIdentifier.register(ctx)
                DeviceID.getOAID(ctx, object : IGetter {
                    override fun onOAIDGetComplete(result: String?) {
                        HeadMgr.instance.deviceId = result!!
                        EasyDataStore.putData(Constant.DS_OAID, result)
                        mPrepared = true
                    }

                    override fun onOAIDGetError(error: Exception?) {
                        val localId = "gs_"+ Build.MODEL+ "_" + UUID.randomUUID()
                        HeadMgr.instance.deviceId = localId
                        EasyDataStore.putData(Constant.DS_OAID, localId)
                        mPrepared = true
                    }

                })
            }

        }

        fun getPackageName()  = "com.goodfather.sdk.textbook.SDKDemo"

        fun getVersionName()  = "1.0.0"

    }
}