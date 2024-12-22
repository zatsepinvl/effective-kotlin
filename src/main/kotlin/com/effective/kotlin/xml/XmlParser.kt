package com.effective.kotlin.xml



fun main() {

    val xmlParser = XmlParser()
    val xml = xmlParser.parse(
        """
        <xml>
            <element1>
                <element2>
                    text
                </element2>
            </element1>
        </xml>
    """.trimIndent()
    )
    println(xml)
}


class XmlParser {

    fun parse(string: String): XmlDocument {
        val trimmed = string.trim()
        val chars = trimmed.toCharArray()
        val root = parseElement(chars, 0, chars.size)
                as? XmlElement
            ?: throw IllegalArgumentException("Invalid XML document")
        return XmlDocument(root)
    }

    private fun parseElement(chars: CharArray, start: Int, end: Int): XmlContent? {
        var tag: XmlTag? = null

        var openTagIndexStart = -1
        var openTagIndexEnd = -1
        var closeTagIndexStart = -1
        var closeTagIndexEnd = -1
        var closedTag = false

        var i = start
        while (i < end && openTagIndexEnd == -1) {
            if (chars[i] == '<') {
                openTagIndexStart = i
            } else if (chars[i] == '>') {
                openTagIndexEnd = i
                tag = parseTag(chars, openTagIndexStart + 1, openTagIndexEnd)
                if (chars[i] == '/') {
                    closedTag = true
                }
                println("Found tag: $tag")
            }
            i++
        }
        if (openTagIndexStart == -1 || openTagIndexEnd == -1) {
            val text = chars
                .slice(start..<end)
                .joinToString("")
                .trim('\n', ' ')
            return XmlText(text)
        }

        i = end - 1
        while (!closedTag && i > openTagIndexEnd && closeTagIndexStart == -1) {
            if (chars[i] == '>') {
                closeTagIndexEnd = i
            } else if (i < end - 1 && chars[i] == '<' && chars[i + 1] == '/') {
                closeTagIndexStart = i
            }
            i--
        }
        if ((closeTagIndexStart == -1 || closeTagIndexEnd == -1) && !closedTag) {
            throw IllegalArgumentException("Invalid xml document from at positions [$start:$end]")
        }

        val content = if (closedTag) {
            null
        } else {
            parseElement(chars, openTagIndexEnd + 1, closeTagIndexStart)
        }

        return XmlElement(tag!!, content)
    }

    private fun parseTag(chars: CharArray, start: Int, end: Int): XmlTag {
        val tag = chars.slice(start..<end).joinToString("")
        return XmlTag(tag, emptyMap())
    }
}


data class XmlDocument(
    val root: XmlElement
)

data class XmlTag(
    val name: String,
    val attributes: Map<String, String>,
)

sealed class XmlContent

data class XmlText(
    val value: String
) : XmlContent()

data class XmlElement(
    val tag: XmlTag,
    /**
     * Content is null in case of a closed tag
     */
    val content: XmlContent?
) : XmlContent()

data class XmlElementList(
    val elements: List<XmlElement>
) : XmlContent()


data class RawTag(
    val name: String,
    val start: Int,
    val end: Int,
    val closed: Boolean,
    val closing: Boolean = false
) {

    companion object {

        fun parseTags(chars: CharArray): MutableList<RawTag> {
            var i = 0
            val openTags = mutableListOf<RawTag>()
            val closeTags = mutableListOf<RawTag>()
            while (i < chars.size) {
                val tag = RawTag.findNext(chars, i, chars.size)
                if (tag != null) {
                    i = tag.end
                    if (tag.closing) {
                        openTags.add(tag)
                    } else {
                        closeTags.add(tag)
                    }
                } else {
                    break
                }
            }
            return openTags
        }

        fun findNext(chars: CharArray, start: Int, end: Int): RawTag? {
            var i = start

            var openTagIndexStart = -1
            var openTagIndexEnd = -1
            var closedTag = false
            var closing = false

            var name: String? = null

            while (i < end && openTagIndexEnd == -1) {
                if (chars[i] == '<') {
                    openTagIndexStart = i
                    if (chars[i + 1] == '/') {
                        closing = true
                    }
                } else if (chars[i] == '>') {
                    openTagIndexEnd = i
                    if (chars[i - 1] == '/') {
                        closedTag = true
                    }
                }
                i++
            }

            if (openTagIndexEnd != -1) {
                var ni = openTagIndexStart
                while (ni < end && chars[ni] != ' ' && chars[ni] != '>') {
                    ni++
                }
                name = chars.slice(openTagIndexStart + 1..ni).joinToString("")
            }

            return if (name != null) {
                RawTag(name, openTagIndexStart, openTagIndexEnd, closedTag, closing)
            } else {
                null
            }
        }
    }
}