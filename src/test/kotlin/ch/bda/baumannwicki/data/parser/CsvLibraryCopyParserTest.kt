package ch.bda.baumannwicki.data.parser

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CsvLibraryCopyParserTest {

    @Test
    fun parseStringToDatas() {
        val expected: List<LibraryCopy> = listOf(LibraryCopy("1", "2", "3", "4"))
        val testString = "1,2,3,4,"
        val testee = CsvLibraryCopyParser()
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        assertEquals(expected, result)
    }

    @Test
    fun parseStringToData() {
        val expected: List<LibraryCopy> = listOf(
            LibraryCopy("1", "2", "3", "4"),
            LibraryCopy("5", "6", "7", "8", "9")
        )
        val testString = "1,2,3,4,\n5,6,7,8,9"
        val testee = CsvLibraryCopyParser()
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        assertEquals(expected, result)
    }

    @Test
    fun parseStringToData2() {
        val expected: List<LibraryCopy> = listOf(
            LibraryCopy("1", "2", "3", "4"),
            LibraryCopy("5", "6", "7", "8", "9"),
            LibraryCopy("3", "5", "7", "6", "2")
        )
        val testString =
            "1,2,3,4,\n5,6,7,8,9\n3,5,7,6,2"
        val testee = CsvLibraryCopyParser()
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        assertEquals(expected, result)
    }

    @Test
    fun parseStringToData3() {
        val expected: List<LibraryCopy> = listOf(
            LibraryCopy("1", "2", "3", "4"),
            LibraryCopy("5", "6", "7", "8", "9"),
            LibraryCopy("3", "5", "7", "6", "2")
        )
        val testString = "1,2,3,4,\n5,6,7,8,9\n3,5,7,6,2"
        val testee = CsvLibraryCopyParser()
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        assertEquals(expected, result)
    }
}