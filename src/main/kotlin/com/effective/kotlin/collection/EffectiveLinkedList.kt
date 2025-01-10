package com.effective.kotlin.collection

import java.util.LinkedList


fun main() {
    val deque = LinkedList<Int>()

    deque.add(1)
    deque.add(2)
    deque.add(3)
    deque.add(4)
    deque.add(5)

    println(deque.pollFirst())
    println(deque.pollLast())
    println(deque.poll())
}