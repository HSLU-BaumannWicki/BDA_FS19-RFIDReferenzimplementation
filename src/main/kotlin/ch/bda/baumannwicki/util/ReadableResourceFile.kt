package util

import java.io.*
import java.net.URL

class ReadableResourceFile(resourceName: String) : ReadableFile {

    private val fileUrl: URL

    init {
        fileUrl =  javaClass.getResource(resourceName)
    }

    override fun getReader(): Reader {
        return fileUrl.openStream().bufferedReader()
    }

    override fun getText(): String {
        return fileUrl.openStream().bufferedReader().readText()
    }
}
