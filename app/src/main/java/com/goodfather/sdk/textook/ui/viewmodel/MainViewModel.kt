package com.goodfather.sdk.textook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goodfather.sdk.textook.bean.BookDetail
import com.goodfather.sdk.textook.bean.SdkError
import com.goodfather.sdk.textook.data.intent.MainIntent
import com.goodfather.sdk.textook.data.repository.MainRepository
import com.goodfather.sdk.textook.data.state.MainState
import com.goodfather.sdk.textook.network.ISdkService
import com.goodfather.sdk.textook.utils.UserMgr
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {

    val mainIntentChannel = Channel<MainIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MainState>(MainState.Idle)

    val state: StateFlow<MainState>
        get() = _state

    init {
        viewModelScope.launch {
            mainIntentChannel.consumeAsFlow().collect {
                when(it) {
                    is MainIntent.OpenBook -> {
                        login(it.partnerUserId, it.productionId)
                    }
                }
            }
        }
    }

    private fun login(partnerUserId: String, produtionId: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                var stateResut: MainState = MainState.Error(SdkError.LoginError)
                mainRepository.login(partnerUserId).also {
                    UserMgr.update(it.data)
                }
                mainRepository.bookDetails(produtionId.toString()).also {
                    stateResut = MainState.BookDetailResp(it.data)
                }
                stateResut
//                MainState.BookDetailResp(BookDetail())
            } catch (e: Exception) {
                e.printStackTrace()
                MainState.Error(SdkError.LoginError)
            }
        }
    }

}