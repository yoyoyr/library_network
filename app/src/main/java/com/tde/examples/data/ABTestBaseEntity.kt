package com.tde.examples.data


/**
 * Desc:ABTest 实验室基础数据结构
 * <p>
 * Date: 2020/9/24
 * author: zhuanghongzhan
 *
 * @param code 状态码
 * @param data 数据
 * @param message 信息
 * @param request_uri
 */
data class ABTestBaseEntity(val code: Int,
                            val data: HashMap<String, ABTestBaseChildDataEntity>?,
                            val message: String,
                            val request_uri: String)


/**
 * Desc: ABTest 基础子类
 * <p>
 * Date: 2020/9/24
 * author: zhuanghongzhan
 *
 * @param id        如果是exp的，则表示用户命中的实验组列表，格式实验id-分组id。
 *                  如果是isol的，则表示用户命中的隔离列表，格式：实验id-隔离域id-是否隔离域（1隔离域，0评估域）
 * @param app_id    应用id
 * @param exp_id    实验id
 * @param exp_key   实验key
 * @param bucket_id 桶id
 * @param group_id  实验组id
 * @param layer_id  分层id
 * @param vars      各自实验带的参数,这边使用map去接受，业务端需要用的话自行根据约定好的key取值
 */
data class ABTestBaseChildDataEntity(val id: String?,
                                     val app_id: Int,
                                     val bucket_id: Int,
                                     val exp_id: Int,
                                     val exp_key: String?,
                                     val group_id: Int,
                                     val layer_id: Int,
                                     val vars: Map<String, String>?)

