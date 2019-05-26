package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test


internal class IntLogPersistorImplementationTest {

    @Test
    fun noMisplacedTagFound() {
        val logPersistor = LogPersistorImplementation()
        logPersistor.noMisplacedTagFound(listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")))
    }

    @Test
    fun misplacedTagFound() {
        val logPersistor = LogPersistorImplementation()
        logPersistor.misplacedTagFound(listOf(LibraryCopy("ZYXW1","1337","Book","Vertical")),listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")))
    }
}