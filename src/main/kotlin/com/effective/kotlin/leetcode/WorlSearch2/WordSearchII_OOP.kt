package com.effective.kotlin.leetcode.WorlSearch2

import kotlin.system.measureTimeMillis

fun main() {
    val solution = WordSearch2SolutionOop()

    var result: List<String>
    val (board, words) = set3
    val time = measureTimeMillis {
        result = solution.findWords(board, words)
    }

    println(result)
    println("time spent $time")
}

class WordSearch2SolutionOop {
    data class TrieNode(
        val nodes: Array<TrieNode?> = Array(26) { null },
        var isWord: Boolean = false
    ) {
        fun next(char: Char): TrieNode? {
            val index = char - 'a'
            return nodes[index]
        }

        fun add(char: Char, node: TrieNode) {
            val index = char - 'a'
            nodes[index] = node
        }

        fun leaveFor(prefix: String): TrieNode? {
            var head = this
            for (char in prefix) {
                val node = head.next(char)
                if (node != null) {
                    head = node
                } else {
                    return null
                }
            }
            return head
        }
    }

    class Trie {
        val root = TrieNode();

        fun insert(word: String) {
            var head = root
            for (char in word) {
                var node = head.next(char)
                if (node == null) {
                    node = TrieNode()
                    head.add(char, node)
                }
                head = node
            }
            head.isWord = true
        }
    }

    class TrieCursor private constructor(private val node: TrieNode) {
        companion object {
            fun withPrefix(prefix: String, trie: Trie): TrieCursor? {
                val node = trie.root.leaveFor(prefix)
                if (node == null) {
                    return null
                }
                return TrieCursor(node)
            }
        }

        fun isWord(): Boolean {
            return node.isWord
        }

        fun next(char: Char): TrieCursor? {
            val nextNode = node.next(char)
            if (nextNode == null) {
                return null
            }
            return TrieCursor(nextNode)
        }
    }

    data class Point(val i: Int, val j: Int) {
        fun move(direction: MoveDirection): Point {
            return when (direction) {
                MoveDirection.UP -> Point(i - 1, j)
                MoveDirection.DOWN -> Point(i + 1, j)
                MoveDirection.LEFT -> Point(i, j - 1)
                MoveDirection.RIGHT -> Point(i, j + 1)
            }
        }
    }

    enum class MoveDirection {
        LEFT, RIGHT, UP, DOWN
    }

    class Board(
        private val board: Array<CharArray>,
        private var visitedPoints: MutableSet<Point> = hashSetOf()
    ) {
        fun visit(point: Point) {
            visitedPoints.add(point)
        }

        fun isVisited(point: Point): Boolean {
            return visitedPoints.contains(point)
        }

        fun unvisit(point: Point) {
            visitedPoints.remove(point)
        }

        fun charOnBoardAt(point: Point): Char {
            return board[point.i][point.j]
        }

        fun withinBoard(point: Point): Boolean {
            return point.i >= 0 && point.i < board.size
                    && point.j >= 0 && point.j < board[0].size
        }
    }

    class BoardCursor private constructor(
        val board: Board,
        val word: String,
        val point: Point
    ) {
        val lastChar: Char get() = word.last()

        companion object {
            fun startAt(board: Board, point: Point): BoardCursor {
                board.visit(point)
                return BoardCursor(
                    board = board,
                    word = board.charOnBoardAt(point).toString(),
                    point = point
                )
            }
        }

        fun move(direction: MoveDirection): BoardCursor? {
            val nextPoint = point.move(direction)
            if (!canMove(nextPoint)) {
                return null
            }

            board.visit(nextPoint)
            val word = word + board.charOnBoardAt(nextPoint)
            return BoardCursor(board, word, nextPoint)
        }

        fun close() {
            board.unvisit(point)
        }

        private fun canMove(point: Point): Boolean {
            return board.withinBoard(point) && !board.isVisited(point)
        }
    }

    fun findWords(board: Array<CharArray>, words: Array<String>): List<String> {
        val trie = Trie()
        for (word in words) {
            trie.insert(word)
        }

        val foundWords = mutableSetOf<String>()
        board.forEachIndexed { i, array ->
            array.forEachIndexed { j, _ ->
                val bboard = Board(board)
                val cursor = BoardCursor.startAt(bboard, Point(i, j))
                val trieCursor = TrieCursor.withPrefix(cursor.word, trie)
                findWords(cursor, trieCursor, foundWords)
            }
        }
        return foundWords.toList()
    }

    private fun findWords(cursor: BoardCursor, trieCursor: TrieCursor?, foundWords: MutableSet<String>) {
        val word = cursor.word
        if (trieCursor == null) {
            return
        }
        if (trieCursor.isWord()) {
            foundWords.add(word)
        }
        MoveDirection.values().forEach { direction ->
            val nextCursor = cursor.move(direction)
            if (nextCursor != null) {
                val nextTrieCursor = trieCursor.next(nextCursor.lastChar)
                findWords(nextCursor, nextTrieCursor, foundWords)
                nextCursor.close()
            }
        }
    }
}