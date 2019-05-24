package ch.bda.baumannwicki.data.parser

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class CsvLibraryCopyParserTest {

    @Test
    fun givenSingleLineString_whenCallingCopyParser_thenListWithOneLibraryCopyWithSame() {
        // arrange
        val libraryCopy = LibraryCopy("1", "2", "3", "4")
        val testString: String = libraryCopy.toString()
        val testee = CsvLibraryCopyParser()
        // act
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        // assert
        assertEquals(listOf(libraryCopy), result)
    }

    @Test
    fun givenTwoLinedString_whenCallingCSVCopyParser_thenListWithTwoLibraryCopyElementsEqualsValuesFromString() {
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
    fun givenThreeLinedString_whenCallingCSVCopyParser_thenShouldGetThreLibraryCopyElementsInListWithValuesFromString() {
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

    @Test
    fun givenSixLinesOfCsvCompliantString_whenCalingParseStringToDatas_thenShouldGetAListWithSixEntries() {
        val testString = "1,2,3,4,\n5,6,7,8,9\n3,5,7,6,2\n,,,,\n,,,,\n,,,,"
        val testee = CsvLibraryCopyParser()
        val result: List<LibraryCopy> = testee.parseStringToDatas(testString)
        assertEquals(6, result.size, "List should has size of 6 but found ${result.size}")
    }


    @Test
    fun givenNotACompliantCSVString_whenCallingParseStringToDatas_thenExpectAnExeption() {
        val testString = "1,2,3\n5,9\n3,5,7,6,2"
        val testee = CsvLibraryCopyParser()
        assertThrows<NotCompliantStringToParseCSVException> { testee.parseStringToDatas(testString) }
    }
}
