package com.harush.david.discoapp

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * Created by David Harush
 * On 21/10/2020.
 */

val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Log.e("coroutineExceptionHandler", "Caught exception in coroutine: $exception")
}

fun View.gone() {
    isVisible = false
}

fun View.visible() {
    isVisible = true
}
