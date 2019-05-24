package util

import org.junit.jupiter.api.Test

class FilenameValidatorTest {
    @Test
    fun IValidFilename() {
        val validFilename = "testfile.txt"
        val testee = FilenameValidator()
        assert(testee.isFilenameValid(validFilename))
    }

}
