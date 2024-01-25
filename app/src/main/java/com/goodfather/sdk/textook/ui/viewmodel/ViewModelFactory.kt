package com.goodfather.sdk.textook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goodfather.sdk.textook.data.repository.MainRepository
import com.goodfather.sdk.textook.network.ISdkService

/**
 * ViewModel工厂
 */
class ViewModelFactory(private val apiService: ISdkService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 判断 MainViewModel 是不是 modelClass 的父类或接口
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiService)) as T
        }
        throw IllegalArgumentException("UnKnown class")
    }
}