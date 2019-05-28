package util

import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader

class ReadableFileStub(val returnValue: String) : ReadableFile {
    override fun getReader(): Reader {
        return InputStreamReader(ByteArrayInputStream(returnValue.toByteArray()))
    }

    override fun getText(): String {
        return returnValue
    }
}
