package com.tde.examples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ned.examples.R
import com.tde.examples.interceptor.TestInterceptor
import com.tde.examples.service.ExampleServices
import com.tde.network.core.NedNetworkManager
import com.tde.network.core.launch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this)[TestViewModel::class.java] }

    private val testClient = NedNetworkManager
        .buildWithBaseUrl("http://localdev.abtest-go.98du.com")
        .addInterceptor(TestInterceptor()).build()

    private val apiService by lazy {
        testClient.createService(ExampleServices::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHttpRequest.setOnClickListener {
            test()
        }


        btnViewModel.setOnClickListener {
            viewModel.testNet()
        }
    }

    private fun test() {
        launch({
            apiService.getABTestData(createHeaderMap())
        }, {
            it.printStackTrace()
        })
    }


    /**
     * 创建请求头部
     */
    private fun createHeaderMap(): HashMap<String, String> {
        val map = HashMap<String, String>()
        map["app-id"] = "1" // 这边 id 写死
        map["x-uid"] = ""
        map["device-id"] = "1233333333"
        map["app-version"] = "3.0.20"
        map["channel"] = "yingyongbao_xysp"
        map["is-new"] = "0"   //是否是新用户0：不是，1：是
        map["os"] = "android"
        return map
    }


}

