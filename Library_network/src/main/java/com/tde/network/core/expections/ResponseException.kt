package com.tde.network.core.expections

import java.lang.Exception

/**
 * 异常Exception 封装
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @param responseCode Int
 * @param responseMessage String?
 * @constructor
 *
 * Author:
 */
class ResponseException(val responseCode: Int, val responseMessage: String?) : Exception(responseMessage)