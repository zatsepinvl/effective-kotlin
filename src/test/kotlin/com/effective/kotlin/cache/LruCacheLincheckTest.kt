package com.effective.kotlin.cache

import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.annotations.StateRepresentation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class LruCacheLincheckTest {

    private val cache = ConcurrentLruCache<Int, Int>(1)

    @Operation
    fun put(key: Int, value: Int) {
        cache.put(key, value)
    }

    @Operation
    fun get(key: Int): Int? {
        return cache.get(key)
    }

    @Operation
    fun remove(key: Int): Int? {
        return cache.remove(key)
    }

    @StateRepresentation
    fun stateRepresentation(): String {
        return cache.toString()
    }

    @Test
    fun stressTest() {
        return StressOptions()
            //.sequentialSpecification(LruCacheSpec::class.java)
            .check(this::class)
    }

    @Test
    fun modelCheckingTest() {
        ModelCheckingOptions().check(this::class)
    }
}

class LruCacheSpec {

    private val lock = ReentrantLock()
    private val cache = ConcurrentLruCache<Int, Int>(1)

    fun put(key: Int, value: Int) = lock.withLock { cache.put(key, value) }

    fun get(key: Int) = lock.withLock { cache.get(key) }

    fun remove(key: Int) = lock.withLock { cache.remove(key) }
}