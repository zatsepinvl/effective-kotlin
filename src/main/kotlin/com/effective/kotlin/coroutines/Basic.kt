package com.effective.kotlin.coroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val animals = listOf("dog", "cat", "lion", "tiger", "elephant")
    runBlocking {
        printAllAnimals(animals)
    }
}

private suspend fun printAllAnimals(animals: List<String>) = coroutineScope {
    animals.forEach { animal ->
        launch {
            val delayMillis = (Math.random() * 2000).toLong()
            printAnimalWithDelay(animal, delayMillis)
        }
    }
}

private suspend fun printAnimalWithDelay(animal: String, delayMillis: Long) {
    delay(delayMillis)
    println(animal)
}

