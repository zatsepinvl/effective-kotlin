package com.effective.kotlin.leetcode.SlidingWindowMaximum

import java.awt.SystemColor.window
import java.util.PriorityQueue

fun main() {
    val solution = Solution()

    val result = solution.maxSlidingWindow(
        intArrayOf(1, 3, -1, -3, 5, 3, 6, 7),
        3
    )

    println(result.contentToString())
}

class Solution {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        val result = IntArray(nums.size - k + 1)
        val queue = PriorityQueue<Int>(k) { a, b -> b - a }
        nums.take(k).forEach { queue.add(it) }
        result[0] = queue.peek()

        for (step in 1..result.lastIndex) {
            queue.remove(nums[step - 1])
            queue.add(nums[step + k -1])
            result[step] = queue.peek()
        }

        return result
    }
}