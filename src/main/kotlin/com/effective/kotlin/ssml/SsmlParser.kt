package com.effective.kotlin.ssml

import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory


// https://cloud.google.com/text-to-speech/docs/ssml
fun main() {
    val ssml = """
        <speak>
          Here are <say-as interpret-as="characters">SSML</say-as> samples.
          I can pause <break time="3s"/>.
          I can play a sound
          <audio src="https://www.example.com/MY_MP3_FILE.mp3">didn't get your MP3 audio file</audio>.
          I can speak in cardinals. Your number is <say-as interpret-as="cardinal">10</say-as>.
          Or I can speak in ordinals. You are <say-as interpret-as="ordinal">10</say-as> in line.
          Or I can even speak in digits. The digits for ten are <say-as interpret-as="characters">10</say-as>.
          I can also substitute phrases, like the <sub alias="World Wide Web Consortium">W3C</sub>.
          Finally, I can speak a paragraph with two sentences.
          <p><s>This is sentence one.</s><s>This is sentence two.</s></p>
        </speak>
    """.trimIndent()
    val parser = SsmlParser()
    val result = parser.parse(ssml)
    parser.print(result)
}

class SsmlParser {

    fun parse(xml: String): SsmlTag {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(xml.byteInputStream())

        document.documentElement.normalize()

        fun parseElement(element: Element): SsmlTag {
            val tagName = element.tagName
            val attributes = element
                .attributes
                .let { attr ->
                    (0 until attr.length).associate {
                        attr.item(it).nodeName to attr.item(it).nodeValue
                    }
                }
            val content = element.textContent
            val children = element.childNodes.let { nodeList ->
                (0 until nodeList.length).mapNotNull {
                    val node = nodeList.item(it)
                    if (node is Element)  {
                        parseElement(node)
                    } else {
                        null
                    }
                }
            }
            return SsmlTag(tagName, attributes, content, children)
        }

        val root = document.documentElement
        return parseElement(root)
    }

    fun print(tag: SsmlTag, indent: String = "") {
        println("$indent<Tag: ${tag.tagName}, \n$indent Attributes: ${tag.attributes}, \n$indent Content: ${tag.content}>")
        for (child in tag.children) {
            print(child, "$indent  ")
        }
    }
}

data class SsmlTag(
    val tagName: String,
    val attributes: Map<String, String> = emptyMap(),
    val content: String? = null,
    val children: List<SsmlTag> = emptyList()
)
