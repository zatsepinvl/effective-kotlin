import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val solution = Solution()

    val result = solution.minWindow("ADOBECODEBANC", "ABC")
    val expected = "BANC"
    println("Result  : $result")
    println("Expected: $expected")
    println(result == expected)
}

class Solution {
    data class Symbol(val char: Char, val index: Int)

    data class Range(val left: Int, val right: Int) {
        val length = right - left + 1

        fun min(other: Range): Range {
            return if (other.length <= this.length) {
                other
            } else {
                this
            }
        }
    }

    class Window(private val chars: MutableList<Char>) {
        private var left: Int = Int.MIN_VALUE
        private var right: Int = Int.MAX_VALUE
        private val priorityQueue = PriorityQueue<Symbol> { a, b -> a.index - b.index }

        fun canAdd(char: Char): Boolean {
            return chars.indexOf(char) >= 0
        }

        fun add(symbol: Symbol) {
            if (left < 0) {
                left = symbol.index
            }
            priorityQueue.add(symbol)
            chars.remove(symbol.char)
            if (chars.isEmpty()) {
                right = symbol.index
            }
        }

        fun isComplete(): Boolean {
            return chars.isEmpty()
        }

        fun range(): Range {
            return Range(left, right)
        }

        fun first(): Symbol {
            return priorityQueue.peek()
        }

        fun isNotEmpty() :Boolean {
            return priorityQueue.isNotEmpty()
        }

        fun moveFirst(newIndex: Int) {
            val char = priorityQueue.poll().char
            val newSymbol = Symbol(char, newIndex)

            priorityQueue.add(newSymbol)

            left = priorityQueue.peek().index
            if (newIndex > right) {
                right = newIndex
            }
        }
    }

    fun minWindow(s: String, t: String): String {
        val chars = t.toMutableList()
        val charsSet = t.toHashSet()
        val window = Window(chars = chars)

        val map: MutableMap<Char, MutableList<Int>> = mutableMapOf()
        var minRange = Range(-1, s.length)

        s.forEachIndexed { index, char ->
            val symbol = Symbol(char, index)
            if (window.canAdd(char)) {
                window.add(symbol)
            } else {
                if (charsSet.contains(char)) {
                    map.getOrPut(char) { ArrayList() }.add(index)
                }
            }
            while (window.isNotEmpty() && map[window.first().char]?.isNotEmpty() == true) {
                val oldSymbol = window.first()
                val newChars = map[oldSymbol.char]!!
                val newCharIndex = newChars.removeAt(0)
                window.moveFirst(newCharIndex)
            }
            if (window.isComplete()) {
                minRange = minRange.min(window.range())
            }
        }

        return if (minRange.left == -1) {
            return ""
        } else {
            s.substring(minRange.left, minRange.right + 1)
        }
    }
}