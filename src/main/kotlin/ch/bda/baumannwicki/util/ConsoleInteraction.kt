package ch.bda.baumannwicki.util

import java.io.InputStream
import java.util.*

open class ConsoleInteraction(val inputStream: InputStream) {
    val scanner = Scanner(inputStream)

    private fun available(): Boolean {
        return inputStream.available() < 0
    }

    open fun nextLine(): String {
        if (inputStream.available() > 0) {
            return Scanner(inputStream).nextLine() ?: ""
        }
        return ""
    }

    fun close() {
        scanner.close()
        inputStream.close()
    }

}
