package com.effective.kotlin.leetcode.PacificAtlanticWaterFlow

fun main() {
    val solution = Solution()

    val result = solution.pacificAtlantic(
        arrayOf(
            intArrayOf(1, 2, 2, 3, 5),
            intArrayOf(3, 2, 3, 4, 4),
            intArrayOf(2, 4, 5, 3, 1),
            intArrayOf(6, 7, 1, 4, 5),
            intArrayOf(5, 1, 1, 2, 4)
        )
    )

    println(result)
}


class Solution {
    data class Point(val x: Int, val y: Int)

    private val pMap: MutableMap<Point, Boolean> = mutableMapOf()
    private val aMap: MutableMap<Point, Boolean> = mutableMapOf()

    fun pacificAtlantic(heights: Array<IntArray>): List<List<Int>> {
        val results: MutableList<List<Int>> = mutableListOf()

        for (x in heights.indices) {
            for (y in heights[x].indices) {
                val point = Point(x, y)
                if (canFlowToPacific(point, heights, mutableSetOf())
                    && canFlowToAtlantic(point, heights, mutableSetOf())
                ) {
                    results.add(listOf(x, y))
                }
            }
        }

        return results
    }

    private fun canFlowToPacific(point: Point, heights: Array<IntArray>, visited: MutableSet<Point>): Boolean {
        val (x, y) = point

        if (pMap.containsKey(point)) {
            return pMap[point]!!
        }

        if (visited.contains(point)) {
            return false
        }
        visited.add(point)

        if (x == 0 || y == 0) {
            pMap[point] = true
            return true
        }

        val height = heights[x][y]

        val canFlowNext = { next: Point ->
            if (next.x > heights.lastIndex || next.y > heights[0].lastIndex) {
                false
            } else if (height >= heights[next.x][next.y]) {
                canFlowToPacific(next, heights, visited)
            } else {
                false
            }
        }

        val result = canFlowNext(Point(x + 1, y))
                || canFlowNext(Point(x - 1, y))
                || canFlowNext(Point(x, y - 1))
                || canFlowNext(Point(x, y + 1))

        pMap[point] = result

        return result
    }

    private fun canFlowToAtlantic(point: Point, heights: Array<IntArray>, visited: MutableSet<Point>): Boolean {
        val (x, y) = point

        if (aMap.containsKey(point)) {
            return aMap[point]!!
        }

        if (visited.contains(point)) {
            return false
        }
        visited.add(point)

        if (x == heights.lastIndex || y == heights[0].lastIndex) {
            aMap[point] = true
            return true
        }

        val height = heights[x][y]

        val canFlowNext = { next: Point ->
            if (next.x < 0 || next.y < 0) {
                false
            } else if (height >= heights[next.x][next.y]) {
                canFlowToAtlantic(next, heights, visited)
            } else {
                false
            }
        }

        val result = canFlowNext(Point(x + 1, y))
                || canFlowNext(Point(x - 1, y))
                || canFlowNext(Point(x, y - 1))
                || canFlowNext(Point(x, y + 1))

        aMap[point] = result

        return result
    }
}