package com.tde.examples.service

import com.tde.examples.data.ABTestBaseEntity
import retrofit2.http.GET
import retrofit2.http.HeaderMap


interface ExampleServices {

    @GET("/v1/init")
    suspend fun getABTestData(@HeaderMap headers: Map<String, String>): ABTestBaseEntity
}