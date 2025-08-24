package controller

import kotlinx.coroutines.CoroutineScope

interface Controller {
    val scope: CoroutineScope
    fun start()
    fun stop()
}