package com.effective.kotlin.cache

import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1, warmups = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
open class LruCacheBenchmark {

    private val cache = LruCache<Int, Int>(100)
    private val concurrentCache = ConcurrentLruCache<Int, Int>(100)

    @Benchmark
    fun testPut() {
        (1..100).forEach { cache.put(it, it) }
    }

    @Benchmark
    fun testConcurrentPut() {
        (1..100).forEach { concurrentCache.put(it, it) }
    }

//    @Benchmark
//    fun testGet() {
//        (1..100).forEach { cache.get(it) }
//    }
//
//    @Benchmark
//    fun testConcurrentGet() {
//        (1..100).forEach { concurrentCache.get(it) }
//    }
}