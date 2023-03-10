package com.effective.kotlin.coroutines

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main() {
    GlobalScope.launch() {
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World!")
    }
}

suspend fun fooSuspend() {
    delay(100);
}