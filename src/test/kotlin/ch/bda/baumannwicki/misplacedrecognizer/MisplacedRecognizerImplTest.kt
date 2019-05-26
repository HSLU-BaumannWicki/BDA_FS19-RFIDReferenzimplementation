package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class MisplacedRecognizerImplTest {

    @Test
    fun givenListWithOneTag_whenCalledGetMisplacedTags_thenReturnShouldBeEmptyList() {
        val testee = MisplacedRecognizerImpl()
        val result: List<LibraryCopy> = testee.getMisplacedTags(listOf(LibraryCopy("F", "1", "1", "2")))
        assertEquals(emptyList(), result)
    }

    @Test
    fun givenListTwoTagsWithSameContent_whenCalledGetMisplacedTags_thenReturnShouldBeEmptyList() {
        val testee = MisplacedRecognizerImpl()
        val result: List<LibraryCopy> = testee.getMisplacedTags(
            listOf(
                LibraryCopy("F", "1", "1", "2"),
                LibraryCopy("F", "1", "1", "2")
            )
        )
        assertEquals(emptyList(), result)
    }

    @Test
    fun givenListThreeTagsWithOneTagWithADiferentBox_whenCalledGetMisplacedTags_thenReturnTheOneTagWithDifferentBox() {
        val testee = MisplacedRecognizerImpl()
        val result: List<LibraryCopy> = testee.getMisplacedTags(
            listOf(
                LibraryCopy("F", "1", "1", "2"),
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "2", "1", "2")
            )
        )
        assertEquals(listOf(LibraryCopy("F", "1", "1", "2")), result)
    }

    @Test
    fun givenTagListContaintingTwoWrongBoxes_whenCalledGetMisplacedTags_thenReturnTheTagsWithDifferentBox() {
        val testee = MisplacedRecognizerImpl()
        val result: List<LibraryCopy> = testee.getMisplacedTags(
            listOf(
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "5", "1", "2"),
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "6", "1", "2")
            )
        )
        assertEquals(listOf(LibraryCopy("F", "5", "1", "2"), LibraryCopy("F", "6", "1", "2")), result)
    }

    @Test
    fun givenTagListContaintingTwoTagsWithDifferentBoxes_whenCalledGetMisplacedTags_thenExceptionIsThrown() {
        val testee = MisplacedRecognizerImpl()
        assertThrows<BoxIdentificationNotPossibleException> {
            testee.getMisplacedTags(
                listOf(
                    LibraryCopy("F", "2", "1", "2"),
                    LibraryCopy("F", "1", "1", "2")
                )
            )
        }
    }

    @Test
    fun givenTagListwithOneTag_whenCalledgetBackRelationBoxId_thenBoxIdFromTagListEntrieShouldReturn() {
        val testee = MisplacedRecognizerImpl()
        val result = testee.getBackRelationBoxId(listOf(LibraryCopy("F", "1", "1", "2")))
        assertEquals("1", result)
    }

    @Test
    fun givenTagListwithTwoTagSameBox_whenCalledgetBackRelationBoxId_thenReturnBoxIdFromBothTag() {
        val testee = MisplacedRecognizerImpl()
        val result = testee.getBackRelationBoxId(
            listOf(
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "2", "1", "2")
            )
        )
        assertEquals("2", result)
    }

    @Test
    fun givenTagListwithManyTags_whenCalledgetBackRelationBoxId_thenReturnBoxIdFromMostAppearingID() {
        val testee = MisplacedRecognizerImpl()
        val result = testee.getBackRelationBoxId(
            listOf(
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "5", "1", "2"),
                LibraryCopy("F", "2", "1", "2"),
                LibraryCopy("F", "6", "1", "2")
            )
        )
        assertEquals("2", result)
    }
}
