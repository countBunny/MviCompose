package com.goodfather.sdk.textook.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types.newParameterizedType
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

inline fun <reified T> toJsonByMoshi(src: T): String {
    val jsonAdapter =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<T>(T::class.java)
    return jsonAdapter.toJson(src)
}

inline fun <reified T> fromJsonToAnyByMoshi(json: String): T? {
    val jsonAdapter =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<T>(T::class.java)
    try {
        return jsonAdapter.fromJson(json)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

inline fun <reified T> fromJsonToListByMoshi(json: String): List<T> {
    val listType = newParameterizedType(List::class.java, T::class.java)
    val jsonAdapter =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<List<T>>(listType)
    return try {
        jsonAdapter.fromJson(json) ?: emptyList()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

inline fun <reified T1, reified T2> fromJsonToMapByMoshi(json: String): Map<T1, T2> {
    val mapType = newParameterizedType(Map::class.java, T1::class.java, T2::class.java)
    val jsonAdapter =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter<Map<T1, T2>>(mapType)
    return try {
        jsonAdapter.fromJson(json) ?: emptyMap()
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}