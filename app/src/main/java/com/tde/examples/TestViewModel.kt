package com.tde.examples

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tde.examples.service.ExampleServices
import com.tde.network.core.NedNetworkManager
import com.tde.network.core.launch

class TestViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService by lazy {
        NedNetworkManager.buildWithBaseUrl("http://localdev.abtest-go.98du.com")
            .createService(ExampleServices::class.java)
    }

    fun testNet() {
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