package com.goodfather.sdk.textook.network

import com.goodfather.sdk.textook.GpapaFacade
import com.goodfather.sdk.textook.utils.UserMgr
import java.security.MessageDigest


class HeadMgr private constructor(){
    var deviceId = ""
    var appId = "1"
    var appKey = "56LymwTAIcEcXzJBRlGiBxSSIQWUTKcd"

    companion object {
        const val PARAM_APP_ID = "appId"
        const val PARAM_APP_KEY = "appKey"
        const val PARAM_PKG_NAME = "packageName"
        const val PARAM_VERSION_NAME = "versionName"
        const val PARAM_ACCESS_TOKEN = "accessToken"
        const val PARAM_USER_ID = "userId"
        const val PARAM_TIME_STAMP = "timestamps"
        const val PARAM_DEVICE_ID = "deviceId"
        const val PARAM_PLATFORM = "platform"
        const val PARAM_SERVER_SIGN = "serverSign"

        val instance = Inner.holder

        fun getHeaders(): List<Pair<String, String>>{
            val mgr = instance
            val user = UserMgr.instance.userData
            /*
            appId=1&deviceId=gs_YDTX20_061e30b9-725f-4bfc-ac2e-dd365fca5a7e
            &packageName=com.goodfather.sdk.textbook.SDKDemo
            &platform=android
            &timestamps=1706092380277
            &userId=1
            &versionName=1.0.0
            &appKey=56LymwTAIcEcXzJBRlGiBxSSIQWUTKcd
             */
            val headerParams = arrayListOf(
                PARAM_APP_ID to mgr.appId,
                PARAM_DEVICE_ID to mgr.deviceId,
                PARAM_PKG_NAME to GpapaFacade.getPackageName(),
                PARAM_PLATFORM to "android",
                PARAM_TIME_STAMP to System.currentTimeMillis().toString(),
            )
            user?.let {
                headerParams += (PARAM_USER_ID to it.userId.toString())
            }
            headerParams += (PARAM_VERSION_NAME to GpapaFacade.getVersionName())
            headerParams += (PARAM_SERVER_SIGN to sign(headerParams))
            user?.let {
                headerParams += (PARAM_ACCESS_TOKEN to it.accessToken)
            }
            return headerParams
        }

        /**
         * MD5消息摘要
         * @throws Exception
         */
        @Throws(Exception::class)
        private fun md5Hex(msg: String): String? {
            //获的MD5消息摘要
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(msg.toByteArray(charset("utf-8")))
            val result = md5.digest()

            //转换成16进制字符串，注意：转换成16进制不是MD5消息摘要中的步骤，所以它不是必须的
            return toHexString(result)
        }


        /**
         * 将字节数组转换成16进制的字符串
         * @param bytes
         * @return
         */
        private fun toHexString(bytes: ByteArray): String? {
            val sb = StringBuffer()
            for (b in bytes) {
                var hex = Integer.toHexString(b.toInt() and 0x0FF)
                if (hex.length == 1) hex = "0$hex"
                sb.append(hex)
            }
            return sb.toString()
        }

        private fun sign(params: List<Pair<String, String>>): String {
            val copy = arrayListOf<Pair<String, String>>() + params + (PARAM_APP_KEY to instance.appKey)

            val sb = StringBuilder()
            copy.forEachIndexed{ index, param ->
                sb.append("${param.first}=${param.second}")
                if (index < copy.size-1) {
                    sb.append("&")
                }
            }

            return md5Hex(sb.toString())?:""
        }
    }

    private object Inner {
        val holder = HeadMgr()
    }
}