package util

import java.io.File
import java.io.IOException

class FilenameValidator {
    fun isFilenameValid(filename: String):Boolean {
        val f = File(filename)
        return try {
            f.canonicalPath
            true
        } catch (exception: IOException) {
            false
        }
    }
}