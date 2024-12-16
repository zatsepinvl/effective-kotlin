package com.effective.kotlin.leetcode

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


internal class SkiplistTest {

    @Test
    fun testBasicAddThenSearch() {
        val skipList = Skiplist()
        for (i in 0..100) {
            skipList.add(i)
        }

        println(skipList.draw())

        for (i in 0..100) {
            assertTrue(skipList.search(2))
        }
        assertFalse(skipList.search(-1))
        assertFalse(skipList.search(101))
    }

    // Test for the following input:
    // ["Skiplist","add","add","add","add","search","erase","search","search","search"]
    // [[],[0],[5],[2],[1],[0],[5],[2],[3],[2]]
    @Test
    fun testBasicAddThenSearchThenErase() {
        val skipList = Skiplist()
        skipList.add(0)
        skipList.add(5)
        skipList.add(2)
        skipList.add(1)

        println(skipList.draw())

        assertTrue(skipList.search(0))
        skipList.erase(5)
        assertTrue(skipList.search(2))
        assertFalse(skipList.search(3))
        assertTrue(skipList.search(2))
    }

    // Test for input:
    // ["Skiplist","add","add","add","add","add","add","add","add","add","add","add","add","add","add","add","add","add","add","search","search","search","add","search","erase","add","add","search","erase","search","add","erase","search","add","search","erase","search","search","search","search","erase","add","search","add","add","search","erase","add","erase","erase","search","erase","add","search","erase","search","erase","erase","add","search","add","search","add","erase","search","search","add","add","add","add","add","erase","search","add","erase","erase","erase","erase","add","search","erase","search","erase","add","add","add","search","search","search","erase","search","search","search","search","search","search","add","erase","add","add","search","search","search","search","add","search","add","add","erase","search","search","add","add","add","erase","add","erase","search","erase","erase","add","erase","search","search","erase","add","erase","search","erase","add","search","add","search","erase","search","erase","add","search","search","search","search","search","search","search","search","search","search","search","search","search","search","search","search","search"]
    // [[],[9],[16],[33],[8],[13],[2],[11],[14],[15],[4],[17],[18],[15],[24],[29],[30],[3],[10],[11],[12],[5],[6],[9],[30],[9],[24],[13],[30],[7],[16],[25],[26],[31],[24],[9],[24],[3],[10],[3],[8],[11],[2],[19],[20],[21],[10],[1],[32],[33],[12],[23],[22],[1],[22],[7],[10],[27],[0],[25],[32],[25],[2],[25],[18],[15],[2],[1],[20],[13],[10],[17],[4],[17],[2],[23],[0],[29],[0],[13],[20],[23],[10],[25],[16],[31],[20],[19],[6],[1],[12],[21],[16],[19],[24],[7],[22],[11],[22],[15],[2],[11],[4],[15],[30],[13],[24],[15],[4],[13],[6],[5],[24],[1],[26],[33],[14],[23],[18],[9],[12],[23],[0],[3],[4],[17],[12],[1],[18],[5],[18],[9],[32],[27],[0],[29],[16],[15],[2],[5],[12],[3],[32],[27],[32],[5],[8],[15],[4],[29],[24],[11],[8],[33]]
    @Test
    fun testComplex() {
        val skipList = Skiplist()
        skipList.add(9)
        skipList.add(16)
        skipList.add(33)
        skipList.add(8)
        skipList.add(13)
        skipList.add(2)
        skipList.add(11)
        skipList.add(14)
        skipList.add(15)
        skipList.add(4)
        skipList.add(17)
        skipList.add(18)
        skipList.add(15)
        skipList.add(24)
        skipList.add(29)
        skipList.add(30)
        skipList.add(3)
        skipList.add(10)
        skipList.add(11)
        skipList.add(12)
        skipList.add(5)
        skipList.add(6)
        skipList.add(9)
        skipList.add(30)
        skipList.add(9)
        skipList.add(24)
        skipList.add(13)
        skipList.add(30)
        skipList.add(7)
        skipList.add(16)
        skipList.add(25)
        skipList.add(26)
        skipList.add(31)
        skipList.add(24)
        skipList.add(9)
        skipList.add(24)
        skipList.add(3)
        skipList.add(10)
        skipList.add(3)
        skipList.add(8)
        skipList.add(11)
        skipList.add(2)
        skipList.add(19)
        skipList.add(20)
        skipList.add(21)
        skipList.add(10)
        skipList.add(1)
        skipList.add(32)
        skipList.add(33)
        skipList.add(12)
        skipList.add(23)
        skipList.add(22)
        skipList.add(1)
        skipList.add(22)
        skipList.add(7)
        skipList.add(10)
        skipList.add(27)
        skipList.add(0)
        skipList.add(25)
        skipList.add(32)
        skipList.add(25)
        skipList.add(2)
        skipList.add(25)
        skipList.add(18)
        skipList.add(15)
        skipList.add(2)
        skipList.add(1)
        skipList.add(20)
        skipList.add(13)

        println(skipList.draw())

        assertTrue(skipList.search(10))
        assertTrue(skipList.search(17))
        assertTrue(skipList.search(4))
        assertTrue(skipList.search(17))
        assertTrue(skipList.search(2))
        assertTrue(skipList.search(23))
        assertTrue(skipList.search(22))
        assertTrue(skipList.search(1))
        assertTrue(skipList.search(22))
        assertTrue(skipList.search(7))
        assertTrue(skipList.search(10))
        assertTrue(skipList.search(27))
        assertTrue(skipList.search(0))
        assertTrue(skipList.search(25))
        assertTrue(skipList.search(32))
        assertTrue(skipList.search(25))
        assertTrue(skipList.search(2))
    }

}