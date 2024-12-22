package com.effective.kotlin.xml

import java.util.LinkedList


fun main() {
    val input = """
        <root key=value1>
            <selfClosing />
            <element1>
                <element2>
                    prefix <text>text1</text> suffix
                </element2>
                 <element2>
                    text2
                </element2>
            </element1>
        </root>
    """.trimIndent()

    val xmlParser = XmlParser()
    val document = xmlParser.parse(input)
    document.print()
}


class XmlParser {

    class XmlRegexpMatch(match: MatchResult) {

        companion object {
            // < - start of tag
            // [1] (/?) - closing tag
            // [2] (\w+) - tag name
            // [3] ([^>]*) - tag content
            // [4] (/?) - self closing
            //  > - end of tag
            // or
            // [5] ([^<]+) - content except start of tag
            fun newRegexp(): Regex {
                return Regex("""<(/?)(\w+)([^>]*)(/?)>|([^<]+)""")
            }
        }

        val isOpening: Boolean = match.groupValues[1].isEmpty()

        val isClosing: Boolean = match.groupValues[1] == "/"

        val tagName: String = match.groupValues[2]

        val isSelfClosing: Boolean = match.groupValues[3].isNotEmpty()

        val attributes: String = match.groupValues[4]

        val content: String = match.groupValues[5]
    }

    fun parse(input: String): XmlDocument {
        val elementsStack = LinkedList<XmlElement>()
        var currentElement: XmlElement? = null
        var rootElement: XmlElement? = null

        fun parseAttributes(attrString: String): Map<String, String> {
            val attrRegex = Regex("""(\w+)="([^"]*)"""")
            return attrRegex.findAll(attrString).associate { it.groupValues[1] to it.groupValues[2] }
        }

        val regex = XmlRegexpMatch.newRegexp()
        for (rawMatch in regex.findAll(input)) {
            val match = XmlRegexpMatch(rawMatch)
            when {
                // Self-closing tag
                match.isOpening && match.isSelfClosing && match.tagName.isNotEmpty() -> {
                    val tagName = match.tagName
                    val attributes = parseAttributes(match.attributes)
                    val newElement = XmlElement(tagName, attributes)
                    currentElement = currentElement?.withChild(newElement) ?: newElement
                    rootElement = currentElement
                }

                // Opening tag
                match.isOpening && match.tagName.isNotEmpty() -> {
                    val tagName = match.tagName
                    val attributes = parseAttributes(match.attributes)
                    val newTag = XmlElement(tagName, attributes)
                    currentElement?.let { elementsStack.add(it) }
                    currentElement = newTag
                }

                // Closing tag
                match.isClosing && match.tagName.isNotEmpty() -> {
                    val closedElement = currentElement
                    rootElement = closedElement
                    currentElement = elementsStack.pollLast()
                    currentElement = currentElement?.withChild(closedElement!!)
                }

                // Content
                match.content.isNotEmpty() -> {
                    val content = match.content.trim()
                    if (content.isNotBlank()) {
                        currentElement = currentElement?.withContent(content)
                    }
                }
            }
        }

        val root = rootElement ?: throw IllegalArgumentException("Invalid XML structure")
        return XmlDocument(root)
    }
}

data class XmlDocument(
    val root: XmlElement
) {

    fun print() {
        doPrint(root)
    }

    private fun doPrint(element: XmlElement, indent: String = "") {
        println(
            "$indent<Tag: ${element.tagName}," +
                    " \n$indent Attributes: ${element.attributes}," +
                    " \n$indent Content: ${element.content}" +
                    "\n$indent>"
        )
        for (child in element.children) {
            doPrint(child, "$indent  ")
        }
    }
}

data class XmlElement(
    val tagName: String,
    val attributes: Map<String, String> = emptyMap(),
    val content: String? = null,
    val children: List<XmlElement> = emptyList()
) {

    fun withContent(content: String): XmlElement {
        return this.copy(content = (this.content ?: "") + content)
    }

    fun withChild(element: XmlElement): XmlElement {
        return this.copy(children = children + element)
    }
}