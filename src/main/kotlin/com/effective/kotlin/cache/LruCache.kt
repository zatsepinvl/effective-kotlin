package com.effective.kotlin.cache

import java.time.Clock
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.annotation.concurrent.ThreadSafe
import kotlin.concurrent.read
import kotlin.concurrent.write

fun main() {

    val cache = LruCache<Int, Int>(4)
    val concurrentCache = ConcurrentLruCache<Int, Int>(4)

    repeat(10) {
        cache.put(it, it)
        concurrentCache.put(it, it)
    }

    repeat(10) {
        println(cache.get(it))
        println(concurrentCache.get(it))
    }
}

@ThreadSafe
class LruCache<K, V>(
    val maxSize: Int,
    val clock: Clock = Clock.systemUTC()
) {

    data class CacheEntry<V>(val value: V, val expiresAt: Long? = null)

    private val cache = object : LinkedHashMap<K, CacheEntry<V>>(maxSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, CacheEntry<V>>?): Boolean {
            return this.size > maxSize
        }
    }

    // Read-write lock makes sense for the case when we have a lot of reads and few writes
    // otherwise simple lock would be better
    private val lock = ReentrantReadWriteLock()

    fun put(key: K, value: V, ttl: Long? = null) {
        lock.write {
            cache[key] = CacheEntry(value, ttl?.plus(clock.millis()))
        }
    }

    fun get(key: K): V? {
        lock.read {
            val entry = cache[key]
                ?: return null
            return if (entry.expiresAt == null || entry.expiresAt < clock.millis()) {
                entry.value
            } else {
                lock.write { cache.remove(key) }
                null
            }
        }
    }

    fun remove(key: K): V? {
        lock.write {
            return cache.remove(key)?.value
        }
    }
}

// 10 times slower than LruCache
@ThreadSafe
class ConcurrentLruCache<K : Any, V>(
    val maxSize: Long,
    val clock: Clock = Clock.systemUTC()
) {

    data class CacheEntry<V>(val value: V, val expiresAt: Long? = null)

    private val accessOrder = LinkedList<K>()
    private val cache = ConcurrentHashMap<K, CacheEntry<V>>()

    private val lock = ReentrantReadWriteLock()

    fun put(key: K, value: V, ttl: Long? = null) {
        lock.write {
            cache[key] = CacheEntry(value, ttl?.plus(clock.millis()))
            updateAccessOrder(key)
            cleanUp()
        }
    }

    fun get(key: K): V? {
        return lock.read {
            val entry = cache[key]
                ?: return null
            if (entry.expiresAt == null || entry.expiresAt > clock.millis()) {
                lock.write {
                    updateAccessOrder(key)
                }
                entry.value
            } else {
                // Clean up expired entry
                lock.write {
                    cache.remove(key)
                    accessOrder.remove(key)
                }
                null
            }
        }
    }

    fun remove(key: K): V? {
        return lock.write {
            accessOrder.remove(key)
            cache.remove(key)?.value
        }
    }

    private fun updateAccessOrder(key: K) {
        accessOrder.remove(key)
        accessOrder.addFirst(key)
    }

    private fun cleanUp() {
        while (cache.size > maxSize) {
            val leastAccessed = accessOrder.pollLast()
            if (leastAccessed != null) {
                cache.remove(leastAccessed)
            }
        }
    }

    override fun toString(): String {
        return cache.toString()
    }
}