package util

import java.io.Reader

interface ReadableFile {
    fun getText(): String

    fun getReader(): Reader
}
