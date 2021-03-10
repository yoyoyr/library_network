package com.tde.examples

import android.app.Application
import com.ned.examples.BuildConfig
import com.tde.network.core.NedNetworkManager
import com.tde.network.core.log.Level
import com.tde.network.core.log.LoggingInterceptor

/**
 * @author wushuifang
 */
class NEDExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initNetServices()
    }

    private fun initNetServices() {
        //添加全局统一拦截器
        NedNetworkManager.addInterceptor(
            LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC) //打印的等级
                .request("Request")
                .response("Response")
                .build()
        ).setDebug(BuildConfig.DEBUG)
    }
}