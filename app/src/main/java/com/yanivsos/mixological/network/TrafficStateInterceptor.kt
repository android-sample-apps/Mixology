package com.yanivsos.mixological.network

import android.net.TrafficStats
import okhttp3.Interceptor
import okhttp3.Response

private const val OK_HTTP_TAG = 111

class TrafficStateInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(OK_HTTP_TAG)
        return chain.proceed(chain.request())
    }
}
