package com.effective.kotlin.deadlock

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.Semaphore

fun main() {
    val lock1 = Semaphore(1)
    val lock2 = Semaphore(1)

    val barrier = CyclicBarrier(2)

    Thread {
        lock1.acquire()
        println("Lock 1 acquired")
        barrier.await()
        lock2.acquire()
    }.start()

    Thread {
        lock2.acquire()
        println("Lock 1 acquired")
        barrier.await()
        lock1.acquire()
    }.start()
}