package com.effective.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
fun main(): Unit = runBlocking {
    val channel = foo()
}

@ExperimentalCoroutinesApi
fun CoroutineScope.foo(): ReceiveChannel<Int> = produce {
    for (i in 1..100) {
        send(1)
        delay(100)
    }
}


