package util

import java.io.File

class WriteableFileImpl(var file: File) : WriteableFile {
    init {
        changeFile(file)
    }

    override fun changeFile(file: File) {
        if (!file.exists()) {
            file.createNewFile()
        }
        this.file = file
    }

    override fun writeToFile(text: String) {
        file.writeText(text)
    }
}
