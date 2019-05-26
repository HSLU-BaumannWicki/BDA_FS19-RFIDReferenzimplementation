package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
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


    class TagBuilder() {
        fun buildTag(box: String): LibraryCopy {
            return LibraryCopy("F", box, "1", "2")
        }

        fun buildListTags(box: String, number: Int): List<LibraryCopy> {
            val list = ArrayList<LibraryCopy>()
            for (i in 0..number) list.add(buildTag(box))
            return list
        }

        fun buildListWithDifferentBoxes(
            box1: String,
            numberOfBoxOne: Int,
            box2: String,
            numberOfBoxTwo: Int
        ): List<LibraryCopy> {
            val list = buildListTags(box1, numberOfBoxOne).toMutableList()
            list.addAll(buildListTags(box2, numberOfBoxTwo))
            return list
        }
    }
}