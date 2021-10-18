package com.example.top100downloaderapp

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class Feeds(val title: String?, val image: String?, val link: String?, val summary: String?) {
    override fun toString(): String = title!!
}


class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): List<Feeds> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readRssFeed(parser)
        }
    }

    private fun readRssFeed(parser: XmlPullParser): List<Feeds> {

        val feeds = mutableListOf<Feeds>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var image: String? = null
                var link: String? = null
                var summary: String? = null


                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "im:name" -> title = readTitle(parser)
                        "im:image" -> image = readImage(parser)
                        "summary" -> summary = readSummary(parser)
                        "id" -> link=readLink(parser)
                        else -> skip(parser)
                    }
                }
                feeds.add(Feeds(title ,image,link,summary))
            } else {
                skip(parser)
            }
        }
        return feeds
    }

    private fun readSummary(parser: XmlPullParser): String? {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val image = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return image
    }


    private fun readLink(parser: XmlPullParser): String? {
       parser.require(XmlPullParser.START_TAG, ns, "id")
        val link = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "id")
        return link
    }

    private fun readImage(parser: XmlPullParser): String? {
        parser.require(XmlPullParser.START_TAG, ns, "im:image")
        val image = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:image")
        return image
    }


    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "im:name")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "im:name")
        return title
    }
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}