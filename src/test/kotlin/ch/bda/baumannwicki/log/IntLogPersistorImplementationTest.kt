package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.logging.FileHandler
import java.util.logging.Logger


internal class IntLogPersistorImplementationTest {
    val dateTime = LocalDateTime.now()
    private var logPersistor : LogPersistorImplementation = LogPersistorImplementation(FileHandler("./RFIDRefImplIntegrationTestLog-${dateTime.year}.${dateTime.monthValue}.${dateTime.dayOfMonth}-${dateTime.hour}.${dateTime.minute}.${dateTime.second}.log"), Logger.getLogger("IntegrationTest"))

    @Test
    fun noMisplacedTagFound() {
        logPersistor.noMisplacedTagFound(listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")))
    }

    @Test
    fun misplacedTagFound() {
        logPersistor.misplacedTagFound(listOf(LibraryCopy("ZYXW1","1337","Book","Vertical")),listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")))
    }
}