package com.effective.kotlin.leetcode.SlidingWindowMaximum

import java.util.*

fun main() {
    val solution = Solution()

    val result = solution.maxSlidingWindow(
        intArrayOf(8, 2, 4, 7, 3, 1, 5, 6, 0, 9),
        2
    )

    println(result.contentToString())
}

class Solution {
    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
        val result = IntArray(nums.size - k + 1)
        val deque: Deque<Int> = LinkedList()

        nums.take(k).forEach { deque.addMax(it) }
        result[0] = deque.first

        for (step in 1..result.lastIndex) {
            if(deque.first == nums[step - 1]) {
                deque.removeFirst()
            }
            deque.addMax(nums[step + k - 1])
            result[step] = deque.first
        }

        return result
    }

    private fun Deque<Int>.addMax(value: Int) {
        while (this.isNotEmpty() && value > this.last) {
            this.removeLast()
        }
        this.addLast(value)
    }

}