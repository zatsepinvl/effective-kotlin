package com.effective.kotlin.leetcode.PacificAtlanticWaterFlow

fun main() {
    val solution = Solution()
//
//    val result = solution.pacificAtlantic(
//        arrayOf(
//            intArrayOf(1, 2, 2, 3, 5),
//            intArrayOf(3, 2, 3, 4, 4),
//            intArrayOf(2, 4, 5, 3, 1),
//            intArrayOf(6, 7, 1, 4, 5),
//            intArrayOf(5, 1, 1, 2, 4)
//        )
//    )
//
//    println(result)

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

    fun pacificAtlantic(heights: Array<IntArray>): List<List<Int>> {
        val pacific: MutableSet<Point> = mutableSetOf()
        val atlantic: MutableSet<Point> = mutableSetOf()

        val lastX = heights.lastIndex
        val lastY = heights[0].lastIndex

        for (x in heights.indices) {
            dfs(Point(x, 0), heights[x][0], heights, pacific)
            dfs(Point(x, lastY), heights[x][lastY], heights, atlantic)
        }

        for (y in heights[0].indices) {
            dfs(Point(0, y), heights[0][y], heights, pacific)
            dfs(Point(lastX, y), heights[lastX][y], heights, atlantic)
        }

        return pacific.asSequence()
            .filter { atlantic.contains(it) }
            .map { listOf(it.x, it.y) }
            .toList()
    }

    private fun dfs(point: Point, prevHeight: Int, heights: Array<IntArray>, visited: MutableSet<Point>) {
        val (x, y) = point

        if (visited.contains(point)) {
            return
        }

        if (x < 0 || y < 0 || x > heights.lastIndex || y > heights[0].lastIndex) {
            return
        }

        val height = heights[x][y]
        if (height < prevHeight) {
            return
        }

        visited.add(point)

        dfs(Point(x + 1, y), height, heights, visited)
        dfs(Point(x - 1, y), height, heights, visited)
        dfs(Point(x, y - 1), height, heights, visited)
        dfs(Point(x, y + 1), height, heights, visited)
    }
}