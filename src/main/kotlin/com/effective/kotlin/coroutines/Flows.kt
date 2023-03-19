package com.effective.kotlin.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@InternalCoroutinesApi
fun main() {
    val flow = fooFlow()

    runBlocking {
        flow
            .flowOn(Dispatchers.Default)
            .map { it + 1 }
            .collect {
                println("$it ${Thread.currentThread().name}")
                delay(100)
            }
    }

}


fun fooFlow(): Flow<Int> = flow {
    println("Emitting 0 from ${Thread.currentThread().name}")
    emit(0)

    println("Emitting 1 from ${Thread.currentThread().name}")
    emit(1)
}