package com.goodfather.sdk.textook.utils

import android.text.TextUtils
import com.goodfather.sdk.textook.bean.UserData

class UserMgr private constructor(){

    init {
        val jsonData:String = EasyDataStore.getData(Constant.DS_USER_DATA, "")
        jsonData.also {
            if (!TextUtils.isEmpty(it)) {
                userData = fromJsonToAnyByMoshi<UserData>(it)
            }
        }
    }
    var userData: UserData? = null

    companion object {
        val instance = Inner.holder

        fun update(userData: UserData?) {
            instance.apply {
                this.userData = userData
            }
            EasyDataStore.putData(Constant.DS_USER_DATA,
                if (userData == null) null else toJsonByMoshi(userData))
        }

        fun logout() {
            update(null)
        }
    }

    private object Inner {
        val holder = UserMgr()
    }
}