package com.tde.network.core


/**
 * 网络请求状态码常量类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 *
 * Author:
 */
object HttpResponseCode {

    /**
     * 成功
     */
    const val HTTP_CODE_SUCCESS = 200

    const val HTTP_CODE_CREATED = 201

    const val HTTP_CODE_UNAUTHORIZED = 401

    const val HTTP_CODE_FORBIDDEN = 403

    const val HTTP_CODE_NOT_FOUND = 404

    /**
     * 未知异常code
     */
    const val HTTP_CODE_UNKNOWN = 99999

}