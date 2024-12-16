//package com.effective.kotlin.xml
//
//fun main() {
//    val xmlParser = XmlParser()
//
//    val xml = xmlParser.parse(
//        """
//        <xml>
//            <element1>
//                <element2>
//                    value
//                </element2>
//            </element1>
//        </xml>
//
//    """
//    )
//
//    println(xml)
//}
//
//class XmlParser {
//
//    fun parse(string: String): XmlDocument {
//        val trimmed = string.trim()
//        val root = parseElement(trimmed.toCharArray(), 0, trimmed.length)
//        return XmlDocument(root)
//    }
//
//    private fun parseElement(chars: CharArray, start: Int, end: Int): XmlElement {
//        var i = start
//        var openTagIndexStart = -1
//        var openTagIndexEnd = -1
//        var closeTagIndexStart = -1
//        var closeTagIndexEnd = -1
//        var tag: XmlTag? = null
//        var element: XmlTag? = null
//        while (i < end) {
//            if (chars[i].isWhitespace()) {
//                i++
//            }
//            if (chars[i] == '<') {
//                openTagIndexStart = i
//            } else if (chars[i] == '>') {
//                openTagIndexEnd = i
//                val tag = parseTag(chars, openTagIndexStart, openTagIndexEnd)
//            } else if (openTagIndexEnd != -1 && chars[i] == '<' && chars[i + 1] == '/') {
//                closeTagIndexStart = i
//            } else if (closeTagIndexStart != i && chars[i] == '>') {
//                closeTagIndexEnd = i
//            }
//            i++
//        }
//        if (openTagIndexStart == -1) {
//            return
//        }
//    }
//
//    private fun parseTag(chars: CharArray, start: Int, end: Int): XmlTag {
//        return XmlTag(chars.subtract(start, end), emptyMap())
//    }
//}
//
//
//data class XmlDocument(
//    val root: XmlElement
//)
//
//data class XmlElement(
//    val tag: XmlTag,
//)
//
//data class XmlTag(
//    val name: String,
//    val attributes: Map<String, String>,
//)
//
//sealed class XmlContent {
//    data class XmlString(val string: String)
//    data class XmlElement(val tag: XmlTag, val content: XmlContent)
//}