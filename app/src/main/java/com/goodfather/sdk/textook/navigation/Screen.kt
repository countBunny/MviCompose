package com.goodfather.sdk.textook.navigation

sealed class Screen(val route: String) {
    object BookDetail: Screen("book/detail")
    object SplashPage: Screen("page/splash")
}