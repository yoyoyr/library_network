package com.tde.network.core

import android.annotation.SuppressLint
import com.tde.network.core.HttpsUtils.UnSafeHostnameVerifier
import com.tde.network.core.HttpsUtils.UnSafeTrustManager
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.security.SecureRandom
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * RetrofitClient    每个baseUrl对应一个client
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param baseUrl String
 * @property retrofit Retrofit?
 * @property enableProxy Boolean?
 * @property mInterceptors MutableList<Interceptor>
 * @property mNetWorkInterceptors MutableList<Interceptor>
 * @property maxTimeOut Long
 * @property sslSocketFactory SSLSocketFactory?
 * @property trustManager X509TrustManager?
 * @constructor
 *
 * Author:
 */
class RetrofitClient(private val baseUrl: String) {

    @Volatile
    private var retrofit: Retrofit? = null

    /**
     * 是否支持代理抓包
     */
    private var enableProxy: Boolean? = null

    /**
     * 应用拦截器
     */
    private val mInterceptors = mutableListOf<Interceptor>()

    /**
     * 网络拦截器
     */
    private val mNetWorkInterceptors = mutableListOf<Interceptor>()

    /**
     * 超时时间
     */
    private var maxTimeOut = 15 * 1000L

    private var sslSocketFactory: SSLSocketFactory? = null

    private var trustManager: X509TrustManager? = null

    /**
     * 添加应用拦截器列表
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @param interceptors Interceptor
     * @return RetrofitCreator
     */
    fun addInterceptors(interceptors: List<Interceptor>): RetrofitClient {
        if (retrofit == null) {
            mInterceptors.addAll(interceptors)
        }
        return this
    }

    /**
     * 添加应用拦截器（单个）
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @return RetrofitCreator
     */
    fun addInterceptor(interceptor: Interceptor): RetrofitClient {
        if (retrofit == null) {
            mInterceptors.add(interceptor)
        }
        return this
    }

    /**
     * 添加网络拦截器列表
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @param interceptors Interceptor
     * @return RetrofitCreator
     */
    fun addNetWorkInterceptors(interceptors: List<Interceptor>): RetrofitClient {
        if (retrofit == null) {
            mNetWorkInterceptors.addAll(interceptors)
        }
        return this
    }

    /**
     * 添加网络拦截器（单个）
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @return RetrofitCreator
     */
    fun addNetWorkInterceptor(interceptor: Interceptor): RetrofitClient {
        if (retrofit == null) {
            mNetWorkInterceptors.add(interceptor)
        }
        return this
    }

    /**
     * 设置是否支持代理抓包，默认是可以
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @param enableProxy Boolean
     * @return RetrofitCreator
     */
    fun setEnableProxy(enableProxy: Boolean): RetrofitClient {
        if (retrofit == null) {
            this.enableProxy = enableProxy
        }
        return this
    }


    /**
     * 设置超时时间
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-02-20
     * @param time Long
     */
    fun setMaxTimeOut(time: Long): RetrofitClient {
        maxTimeOut = time
        return this
    }

    /**
     * 设置证书
     * <p>
     * Author: zhuanghongzhan
     * Date: 2021-03-02
     * @param sslSocketFactory SSLSocketFactory
     * @param trustManager X509TrustManager
     */
    fun sslSocketFactory(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): RetrofitClient {
        this.sslSocketFactory = sslSocketFactory
        this.trustManager = trustManager
        return this
    }

    /**
     * 创建api代理类
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-10
     * @param service Class<T>
     * @return T
     */
    fun <T> createService(service: Class<T>): T {
        if (retrofit == null) {
            build()
        }
        return retrofit!!.create(service)
    }


    /**
     * 构建
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-09
     * @return Retrofit
     */
    @Synchronized
    fun build(): RetrofitClient {
        if (retrofit == null) {

            val okHttpBuilder = okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory(), UnSafeTrustManager)
                .hostnameVerifier(UnSafeHostnameVerifier)
                .readTimeout(maxTimeOut, TimeUnit.MILLISECONDS)
                .connectTimeout(maxTimeOut, TimeUnit.MILLISECONDS)
                .writeTimeout(maxTimeOut, TimeUnit.MILLISECONDS)
                .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))

            mInterceptors.addAll(NedNetworkManager.getInterceptors())
            NedNetworkManager.getLogInterceptors()?.run {
                mInterceptors.add(this)
            }
            mInterceptors.forEach { okHttpBuilder.addInterceptor(it) }

            mNetWorkInterceptors.forEach { okHttpBuilder.addNetworkInterceptor(it) }

            if (enableProxy == null && !NedNetworkManager.isEnableProxy()) {
                okHttpBuilder.proxy(Proxy.NO_PROXY)
            } else if (enableProxy != null) {
                if (enableProxy == false) {
                    okHttpBuilder.proxy(Proxy.NO_PROXY)
                }
            }

            if (sslSocketFactory != null && trustManager != null) {
                okHttpBuilder.sslSocketFactory(sslSocketFactory!!, trustManager!!)
            }

            retrofit = Retrofit.Builder()
                .client(okHttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
        }
        return this
    }


    /**
     * 释放资源
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-11
     */
    fun release() {
        retrofit = null
        mInterceptors.clear()
        mNetWorkInterceptors.clear()
    }


    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private fun createSSLSocketFactory(): SSLSocketFactory {

        val sc = SSLContext.getInstance("TLS")
        sc.init(
            null, arrayOf<X509TrustManager>(UnSafeTrustManager),
            SecureRandom()
        )
        return sc.socketFactory
    }

//    private class TrustAllManager : X509TrustManager {
//        @Throws(CertificateException::class)
//        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
//        }
//
//        @Throws(CertificateException::class)
//        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
//        }
//
//        override fun getAcceptedIssuers(): X509Certificate {
//             return object :X509Certificate(){};
//        }
//    }

    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}

