package com.goodfather.sdk.textook.network

import android.content.Context
import android.net.ConnectivityManager
import com.goodfather.sdk.textook.GpapaFacade
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * 网络工具类
 */
object NetworkUtils {

    private const val BASE_URL = "http://172.16.2.187:8922/"

    private val httpCacheDirectory = File(GpapaFacade.ctx.cacheDir, "goodfather_Cache")
    private val cache = Cache(httpCacheDirectory, 1024 * 1024 * 10) //10m

    private val rewriteCacheControlInterceptor = object :Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalResponse = chain.proceed(chain.request())
            if (isNetworkAvailable()) {
                val maxAge = 2
                return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .removeHeader("User-Agent")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28
                return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build()
            }
        }

    }


    /**
     * 通过Moshi 将JSON转为为 Kotlin 的Data class
     */
    private val moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * 构建Retrofit
     */
    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getOkHttpClient())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private fun getOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLogInterceptor(GpapaFacade.TAG)
        logInterceptor.colorLevel = Level.INFO
        val interceptorParams = Interceptor { chain ->
            /**
             *
            (1)Mozilla/5.0 ：表示兼容Mozilla, 几乎所有的浏览器都有这个字符;
            (2) (Linux; Android 8.1.0; PACM00 Build/O11019; wv): 表示设备的操作系统版本，以及CPU信息；
            (3）AppleWebKit/537.36 (KHTML, like Gecko)：表示浏览器的内核；
            (4) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36: 表示浏览器的版本号。
             */
            /**
             *
            (1)Mozilla/5.0 ：表示兼容Mozilla, 几乎所有的浏览器都有这个字符;
            (2) (Linux; Android 8.1.0; PACM00 Build/O11019; wv): 表示设备的操作系统版本，以及CPU信息；
            (3）AppleWebKit/537.36 (KHTML, like Gecko)：表示浏览器的内核；
            (4) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36: 表示浏览器的版本号。
             */
//                .addHeader("Referer","https://www.bilibili.com/")
//                .addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
//                .addHeader("Origin","https://www.bilibili.com")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Language","zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
//                .addHeader("Cookie",cookie)
            val newBuilder = chain.request().newBuilder().apply {
                HeadMgr.getHeaders().forEach {
                    addHeader(it.first, it.second)
                }
            }
                .build()
            chain.proceed(newBuilder)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptorParams)
            .addInterceptor(logInterceptor)
            .addNetworkInterceptor(rewriteCacheControlInterceptor)
            .connectTimeout(3 * 1000L, TimeUnit.MILLISECONDS)
            .writeTimeout(3 * 1000L, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .cache(cache)
            .build()
    }

    /**
     * 创建Api网络请求服务
     */
    val sdkService: ISdkService = getRetrofit().create(ISdkService::class.java)

    fun isNetworkAvailable(): Boolean {
        val manager = GpapaFacade.ctx.getApplicationContext().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val available = manager.activeNetworkInfo?.isAvailable?:false
        return available
    }
}