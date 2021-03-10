package com.tde.examples.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class TestInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("testid", "please ignore,this is test")
        return chain.proceed(builder.build())
    }
}