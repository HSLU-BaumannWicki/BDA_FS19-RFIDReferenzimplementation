package util

import java.io.File

class WriteableFileStub : WriteableFile {
    var textStoredToFile: String = ""
    var filename: String = "default"
    override fun writeToFile(text: String) {
        textStoredToFile += text
    }

    override fun changeFile(file: File) {
        filename = file.name
    }

}
