package com.example.f1liveinfo.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

private const val TAG = "LoggingInterceptor"

class LoggingInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        Log.i(TAG, "Request URL: ${request.url}")

        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source?.buffer
        val contentType = responseBody?.contentType()
        val charset: Charset =
            contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        val responseBodyString = buffer?.clone()?.readString(charset)

        Log.i(TAG, "JSON Response: $responseBodyString") // Log the response body

        return response
    }
}