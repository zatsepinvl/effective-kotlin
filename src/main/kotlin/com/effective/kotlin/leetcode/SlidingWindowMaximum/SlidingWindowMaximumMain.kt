package com.effective.kotlin.leetcode.SlidingWindowMaximum

import java.awt.SystemColor.window

fun main() {
    val solution = Solution()

    val result = solution.maxSlidingWindow(
        intArrayOf(1,3,-1,-3,5,3,6,7),
        3
    )

    println(result.contentToString())
}

class Solution {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        val result = IntArray(nums.size - k + 1)
        var lastMax = nums.take(k).max()!!

        for (step in result.indices) {
            var max = nums[step + k - 1]
            if (max < lastMax) {
                for (i in step until step + k) {
                    if (nums[i] > max) {
                        max = nums[i]
                    }
                }
            }
            lastMax = max
            result[step] = max
        }

        return result
    }
}