package util

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntWriteableFileImplTest {

    @Disabled
    @Test
    fun writeToFile() {
        val myFile = File("./Test.txt")
        assertFalse(myFile.exists())
        var writableFile = WriteableFileImpl(myFile)
        writableFile.writeToFile("Hello World!")
        assertTrue(myFile.exists())
        assertEquals(myFile.readText(), "Hello World!")
        myFile.delete()
    }

    @Test
    fun someTest() {
        "E0".toLong(radix = 16).toByte()
        "04".toLong(radix = 16).toByte()
        "01".toLong(radix = 16).toByte()
        "50".toLong(radix = 16).toByte()
        "4F".toLong(radix = 16).toByte()
        "1F".toLong(radix = 16).toByte()
        "FF".toLong(radix = 16).toByte()
        "BB".toLong(radix = 16).toByte()
        val str = "E00401504F1FFFBB"
        for (i in 0..str.length / 2 - 1) {
            str.substring(i * 2, i * 2 + 2).toLong(radix = 16).toByte()
        }
    }
}
