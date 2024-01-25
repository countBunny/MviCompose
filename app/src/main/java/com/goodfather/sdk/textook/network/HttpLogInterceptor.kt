package com.goodfather.sdk.textook.network

import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http.promisesBody
import okio.Buffer
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

class HttpLogInterceptor(tag: String): Interceptor {
    private lateinit var logger: Logger
    var colorLevel = java.util.logging.Level.INFO
    var printLevel: Level = Level.BODY
    init {
        logger = Logger.getLogger(tag)
    }

    private fun log(message: String) {
        logger.log(colorLevel, message)
    }

    companion object{
        val UTF8 = Charsets.UTF_8

        private fun isPlainText(mediaType: MediaType?): Boolean {
            return mediaType?.let {
                if ("text".equals(it.type)) {
                    return@let true
                } else {
                    val subType = it.subtype
                    subType.let {
                        val subTypeLo = it.lowercase()
                        (subTypeLo.contains("x-www-form-urlencoded")
                                or subTypeLo.contains("json")
                                or subTypeLo.contains("xml")
                                or subTypeLo.contains("html")
                                )
                    }
                }

            }?: false
        }

        private fun getCharset(mediaType: MediaType?)  = mediaType?.charset(UTF8)?: UTF8
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        //req params
        logForRequest(request, chain.connection())

        val startNs = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            log("<-- HTTP FAILED: ${e}");
            e.printStackTrace()
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        return logForResponse(response, tookMs)
    }

    private fun logForResponse(response: Response, tookMs: Long): Response {
        val newBuilder = response.newBuilder()
        val clone = newBuilder.build()
        val body = clone.body
        val logBody = printLevel == Level.BODY
        val logHeaders = (printLevel == Level.BODY) or (printLevel == Level.HEADERS)
        log("<-- ${clone.code}  ${clone.message}  ${clone.request.url}  (${tookMs}ms)")
        if (logHeaders) {
            clone.headers.forEach {
                log("\t ${it.first} : ${it.second}")
            }
            log("---headers end--- ")
        }
        if (logBody && clone.promisesBody()) {
            if (isPlainText(body?.contentType())) {
                body?.source()?.readString(getCharset(body.contentType())).let {
                    log("\tbody:$it")
                    it?:""
                }.toResponseBody(body!!.contentType()).let {
                    return response.newBuilder().body(it).build()
                }
            } else {
                log("\tbody: maybe [binary body], omitted!")
            }
        }
        return response
    }

    private fun logForRequest(request: Request, connection: Connection?) {
        val logBody = (printLevel === Level.BODY)
        val logHeaders = (printLevel === Level.BODY) || (printLevel === Level.HEADERS)
        val hasBody = request.body != null
        val protocol = connection?.protocol()?: Protocol.HTTP_1_1
        log("--> ${request.method}  ${request.url}  $protocol")
        if (logHeaders) {
            request.headers.forEach {
                if (!"Content-Type".equals(it.first, ignoreCase = true)
                    && !"Content-Length".equals(it.first, ignoreCase = true)) {
                    log("\t ${it.first} : ${it.second}")
                }
            }
            log("---headers end--- ")
            if (hasBody) {
                request.body?.also {
                    log("\tContent-Type: ${it.contentType()}")
                    log("\tContent-Length: ${it.contentLength()}")
                }


                if (logBody) {
                    if (isPlainText(request.body?.contentType())) {
                        bodyToString(request)
                    } else {
                        log("\tbody: maybe [binary body], omitted!")
                    }
                }
            }
        }

    }

    private fun bodyToString(request: Request) {
        val copy = request.newBuilder().build()
        copy.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            val charset = getCharset(it.contentType())
            log("\tbody:" + buffer.readString(charset))
        }
    }
}

sealed class Level {
    object NONE: Level()
    object BASIC: Level()
    object HEADERS: Level()
    object BODY: Level()
}