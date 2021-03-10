package com.tde.network.core.entity

import com.tde.network.core.HttpResponseCode
import com.tde.network.core.expections.ResponseException

/**
 * 接口基础返回类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * @param T
 * @param code Int
 * @param data T?
 * @param message String
 * @constructor
 *
 * Author:
 */
data class BaseResponseEntity<T>(val code: Int, val data: T?, val message: String) {


    /**
     * 校验data数据的有效性
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @return T
     */
    fun check(): T {
        val tmp = checkOrNull()
        if (tmp != null) {
            return tmp
        }
        throw ResponseException(code, "Response Data Is Null")
    }

    /**
     * 返回到message和code，data信息
     * Author: yangrong
     * Date: 2020-12-23
     * @return BaseResponseEntity<T>
     */
    fun checkWidthMessage(): BaseResponseEntity<T> {
        val tmp = checkOrNull()
        if (tmp != null) {
            return this
        }
        throw ResponseException(code, "Response Data Is Null")
    }


    /**
     * 校验data数据的有效性 即使data为空也不会报错
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @return T?
     */
    fun checkOrNull(): T? {
        if (isSuccess()) {
            return data
        }
        throw ResponseException(code, message)
    }

    /**
     * 判断是否成功
     * <p>
     * Author: zhuanghongzhan
     * Date: 2020-12-18
     * @return Boolean
     */
    fun isSuccess(): Boolean {
        return code == HttpResponseCode.HTTP_CODE_SUCCESS
    }


}


