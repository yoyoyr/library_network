package com.tde.network.core

import okhttp3.Interceptor

/**
 * 网络请求库统一管理类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
object NedNetworkManager {

    private val mServicesOfRetrofit = mutableMapOf<String, RetrofitClient>()

    /**
     * 全局设置的拦截器，对所有域名通用
     */
    private val mInterceptors = mutableListOf<Interceptor>()

    private var logInterceptor: Interceptor? = null

    /**
     * 是否支持代理抓包
     */
    private var enableProxy = true


    /**
     * 是否是开发阶段
     */
    private var isDebug = false

    /**
     * 添加应用拦截器列表
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @param interceptors Interceptor
     * @return RetrofitCreator
     */
    fun addInterceptors(interceptors: List<Interceptor>): NedNetworkManager {
        mInterceptors.addAll(interceptors)
        return this
    }

    /**
     * 添加应用拦截器（单个）
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @return RetrofitCreator
     */
    fun addInterceptor(interceptor: Interceptor): NedNetworkManager {
        mInterceptors.add(interceptor)
        return this
    }

    fun addLogInterceptor(interceptor: Interceptor): NedNetworkManager {
        logInterceptor = interceptor
        return this
    }

    /**
     * 获取全局应用拦截器
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-10
     * @return List<Interceptor>
     */
    fun getInterceptors(): List<Interceptor> {
        return mInterceptors
    }

    /**
     * 将日志拦截器放在所有拦截器下面
     * Author: yangrong
     * Date: 2020-12-24
     * @return Interceptor?
     */
    fun getLogInterceptors(): Interceptor? {
        return logInterceptor
    }


    /**
     * 设置是否是开发debug模式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @param debug Boolean
     * @return NedNetworkManager
     */
    fun setDebug(debug: Boolean): NedNetworkManager {
        this.isDebug = debug
        return this
    }

    /**
     * 是否是debug模式
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @return Boolean
     */
    fun isDebug(): Boolean {
        return isDebug
    }

    /**
     * 设置是否支持代理抓包，默认是可以
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @param enableProxy Boolean
     * @return NedNetworkManager
     */
    fun setEnableProxy(enableProxy: Boolean): NedNetworkManager {
        this.enableProxy = enableProxy
        return this
    }

    /**
     * 判断是否支持代理抓包
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @return Boolean
     */
    fun isEnableProxy(): Boolean {
        return enableProxy
    }


    /**
     * 根据baseUrl 创建不同的RetrofitCreator   如果有则直接使用，如果没有则创建
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @param baseUrl String
     * @return RetrofitCreator
     */
    @Synchronized
    fun buildWithBaseUrl(baseUrl: String): RetrofitClient {
        if (baseUrl.isBlank()) {
            throw IllegalArgumentException("BaseUrl 不能为空!!")
        }
        var mRetrofitClient = mServicesOfRetrofit[baseUrl]
        if (mRetrofitClient == null) {
            mRetrofitClient = RetrofitClient(baseUrl)
            mServicesOfRetrofit[baseUrl] = mRetrofitClient
        }
        return mRetrofitClient
    }

    /**
     * 移除指定baseUrl对应的RetrofitClient
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     * @param baseUrl String
     * @return NedNetworkManager
     */
    fun removeWithBaseUrl(baseUrl: String): NedNetworkManager {
        val mRetrofitClient = mServicesOfRetrofit.remove(baseUrl)
        mRetrofitClient?.release()
        return this
    }


    /**
     * 移除所有的RetrofitClient
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     */
    fun clear() {
        mServicesOfRetrofit.forEach {
            it.value.release()
        }
        mServicesOfRetrofit.clear()
    }

}