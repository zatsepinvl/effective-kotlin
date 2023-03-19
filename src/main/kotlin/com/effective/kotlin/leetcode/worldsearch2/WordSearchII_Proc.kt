package com.effective.kotlin.leetcode.worldsearch2

import java.util.Arrays
import kotlin.system.measureTimeMillis

fun main() {
    val solution = WordSearchSolutionProc()

    var result: List<String>
    val (board, words) = set1
    val time = measureTimeMillis {
        result = solution.findWords(board, words)
    }

    println(result)
    println("time spent $time")
}

class WordSearchSolutionProc {
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

    fun findWords(board: Array<CharArray>, words: Array<String>): List<String> {
        val trie = Trie()
        for (word in words) {
            trie.insert(word)
        }

        val visited = Array(board.size) { Array(board[0].size) { false } }
        val foundWords = mutableSetOf<String>()
        fun dfs(node: TrieNode, i: Int, j: Int, prefix: String) {
            if (i < 0 || i >= board.size || j < 0 || j >= board[0].size) {
                return
            }
            if (visited[i][j]) {
                return
            }

            val char = board[i][j]
            val nextNode = node.next(char)
            if (nextNode == null) {
                return
            }
            val word = prefix + char
            if (nextNode.isWord) {
                foundWords.add(word)
            }

            visited[i][j] = true

            dfs(nextNode, i + 1, j, word)
            dfs(nextNode, i - 1, j, word)
            dfs(nextNode, i, j + 1, word)
            dfs(nextNode, i, j - 1, word)

            visited[i][j] = false
        }
        board.forEachIndexed { i, array ->
            array.forEachIndexed { j, _ ->
                dfs(trie.root, i, j, "")
            }
        }
        return foundWords.toList()
    }
}