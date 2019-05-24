package util

import java.io.File

interface WriteableFile {
    fun writeToFile(text: String)
    fun changeFile(file: File)
}
