# NEDNetworking
安卓网络请求库, 使用okhttp+retrofit+协程


## 1、maven版本
* 基础代码库： `com.ned.network:core-new:0.0.1`


## 2、添加依赖
主项目(app) build.gradle里面添加依赖
```groovy
implementation "com.ned.network:core-new:0.0.1"
```

## 3、使用姿势
#### 1、初始化，如果只有只需要一个baseUrl，可以参考下面的代码进行构建，建议在application里面初始化
```kotlin
 NedNetworkManager.addInterceptor( //设置全局的拦截器
            LoggingInterceptor.Builder() //设置日志打印
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC) //打印的等级
                .request("Request")
                .response("Response")
                .build()
        ).isDebug(BuildConfig.DEBUG)
```
使用以下方法创建retrofi，内部根据baseUrl进行存储对应的retrofitClient

```kotlin
NedNetworkManager
        .buildWithBaseUrl("http://localdev.abtest-go.98du.com")
        .addInterceptor(..) //针对该retrofitClient设置拦截器。注意，该设置只会在第一次时生效
        .addInterceptor(TestInterceptor()).build()
```

注意：设置的拦截器或其他配置项只会在第一次创建retrofitClient时生效。如果是基础的url，则可以在application初始化时进行创建

#### 2、网络请求：
1、创建retrofit代理接口类

```kotlin
interface ExampleServices {

    @GET("/v1/init")
    suspend fun getABTestData(@HeaderMap headers: Map<String, String>): ABTestBaseEntity
}
```

2、创建api代理对象
```kotlin
     private val apiService by lazy { testClient.createService(ExampleServices::class.java) }

```

或
```kotlin
 private val apiService by lazy {
        NedNetworkManager.buildWithBaseUrl("http://localdev.abtest-go.98du.com")
            .createService(ExampleServices::class.java)
    }
```


3、接口调用
使用协程进行调用，如果是在activity、fragment或viewmodel里面调用时可以使用该方法
```kotlin
 launch({ 
            apiService.getABTestData(createHeaderMap())
        }, {
            it.printStackTrace()
        })
```

如果是其他类调用的,可以使用GlobalScope.safeLaunch，这个对异常进行了catch。由于协程作用域的问题，在其他类调用时需要自行管理好，避免泄露或其他问题。建议在viewModel或者dataSource里面进行操作。
```kotlin
GlobalScope.safeLaunch({
            apiService.getABTestData(createHeaderMap())
        },{
            it.printStackTrace()
        })
```


## 4、JBDNetWorkManager构建配置说明
1、支持设置拦截器
```kotlin
    NedNetworkManager.addInterceptor( //设置全局的拦截器
            LoggingInterceptor.Builder() //设置日志打印
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC) //打印的等级
                .request("Request")
                .response("Response")
                .build()
        )
```

2、设置是否支持代理
```kotlin
    NedNetworkManager.setEnableProxy(Boolean)
```


## 未来计划
1、完善异常逻辑处理



## Author

庄宏展, zhuanghongzhan@98du.com

## License

