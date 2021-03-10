package com.tde.network.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 *  基于LifecycleObserver 处理协程的类
 * <p>
 * Date: 2021-03-10
 * Company:
 * Updater:
 * Update Time:
 * Update Comments:
 * @property mLifecycleOwner LifecycleOwner
 * @constructor
 *
 * Author:
 */
open class SimpleLifecycleObserver(owner: LifecycleOwner) : LifecycleObserver {

    private val mLifecycleOwner: LifecycleOwner = owner

    init {
        owner.lifecycle.addObserver(this)
    }

    /**
     * 在LifecycleOwner的onDestroy之前触发
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        mLifecycleOwner.lifecycle.removeObserver(this)
    }


}