package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
import java.util.logging.Logger
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LogPersistorImplTest {

    @Test
    fun givenMisplacedTagsSameAsFoundTags_whenCallingMisplacedTagFound_thenLoggerShouldContainSameSizeForBothEntries() {
        val logger = LoggerStub()
        val testDatas: List<LibraryCopy> =
            listOf(
                LibraryCopy("ABCD1", "1", "Book", "Horizontal"),
                LibraryCopy("ABCD2", "1", "Book", "Horizontal"),
                LibraryCopy("ABCD3", "1", "Book", "Horizontal")
            )

        val testee = LogPersistorImpl(logger)
        testee.misplacedTagFound(testDatas, testDatas)
        assertEquals(
            logger.warnings.size,
            logger.infos.size - 1,
            "Warnings an Infos should be equal in the size of the arra"
        )
    }

    @Test
    fun givenMisplacedTagsWithOneSpecialChar_whenCallingMisplacedTagFound_thenLoggerShouldContainOneSpecialCharInItsLogState() {
        val logger = LoggerStub()
        val testDatas: List<LibraryCopy> =
            listOf(
                LibraryCopy("ABCD1", "1$", "Book", "Horizontal"),
                LibraryCopy("ABCD2", "1", "Book", "Horizontal"),
                LibraryCopy("ABCD3", "1", "Book", "Horizontal")
            )

        val testee = LogPersistorImpl(logger)
        testee.misplacedTagFound(testDatas, testDatas)
        assertTrue(
            logger.warnings.filter { it.contains("$") }.count() >= 1,
            "List should contain at least 1 Dollar sign"
        )
    }

    private class LoggerStub : Logger("", null) {
        val warnings: MutableList<String> = ArrayList()
        val infos: MutableList<String> = ArrayList()

        override fun warning(msg: String?) {
            warnings.add(msg ?: "")
        }

        override fun info(msg: String?) {
            infos.add(msg ?: "")
        }
    }
}