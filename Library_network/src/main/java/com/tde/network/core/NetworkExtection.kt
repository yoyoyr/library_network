package com.tde.network.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tde.network.core.HttpResponseCode.HTTP_CODE_UNKNOWN
import com.tde.network.core.expections.ResponseException
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext


/**
 * 拓展ViewModel，统一viewModelScope调用方式
 *
 * 栗子：
 * launch({
 *      // api请求
 * },{
 *      //fail exception
 * })
 * <p>
 * Author:
 * Date: 2021-03-10
 * @receiver ViewModel
 * @param block SuspendFunction1<[@kotlin.ParameterName] CoroutineScope, Unit>
 * @param fail Function1<[@kotlin.ParameterName] ResponseException, Unit>
 * @return Job
 */
fun ViewModel.launch(
    block: suspend (coroutineScope: CoroutineScope) -> Unit,
    fail: (t: ResponseException) -> Unit = { }
) =
    viewModelScope.safeLaunch(block, fail)

fun ViewModel.async(
    block: suspend (coroutineScope: CoroutineScope) -> Unit
): Job {
    val coroutineScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main)
    val job = viewModelScope.async { block(coroutineScope) }
    return job
}

/**
 * 统一catch viewModelScope
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-11
 * @receiver CoroutineScope
 * @param block 挂起函数
 * @param fail 异常回调
 * @return Job
 */
fun CoroutineScope.safeLaunch(
    block: suspend (coroutineScope: CoroutineScope) -> Unit,
    fail: (t: ResponseException) -> Unit = { }
): Job {
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        fail(ResponseException(HTTP_CODE_UNKNOWN, exception.message))
    }
    return launch(exceptionHandler) { block(this) }
}

/**
 * 同步执行的协程操作
 * 栗子：
 * launch({
 *  val suspend1 = it.safeAsync({
 *      model.doSuspend1()
 *  })
 * val suspend2 = it.safeAsync({
 *      model.doSuspend2()
 * })
 * //两个请求同时进行
 * val suspend1 = suspend1.await()
 * val suspend2 = suspend2.await()
 * })
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-11
 */
fun <T> CoroutineScope.safeAsync(
    block: suspend () -> T,
    fail: (t: ResponseException) -> Unit = { }
) = this.async {
    var result: T? = null
    try {
        result = block()
    } catch (t: Throwable) {
        t.printStackTrace()
        fail(ResponseException(HTTP_CODE_UNKNOWN, t.message))
    }
    result
}

/**
 * LifecycleOwner可使用这个launch
 * <p>
 * Author: zhuanghongzhan
 * Date: 2020-12-11
 * @receiver LifecycleOwner
 * @param block 挂起函数
 * @param fail 异常回调
 * @return Job
 */
fun LifecycleOwner.launch(
    block: suspend CoroutineScope.() -> Unit,
    fail: (t: Throwable) -> Unit = { }
): Job {
    val coroutineScope = CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main)
    val job = coroutineScope.safeLaunch(block, fail)

    object : SimpleLifecycleObserver(this) {
        override fun onDestroy() {
            super.onDestroy()
            coroutineScope.close()
        }
    }
    return job
}


/**
 * 可关闭的CoroutineScope
 * <p>
 * Date: 2020-12-11
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author: zhuanghongzhan
 */
internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}


