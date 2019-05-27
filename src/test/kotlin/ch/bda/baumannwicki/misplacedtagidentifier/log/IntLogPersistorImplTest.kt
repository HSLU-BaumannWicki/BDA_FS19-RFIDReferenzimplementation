package ch.bda.baumannwicki.misplacedtagidentifier.log

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.FileHandler
import java.util.logging.Logger


internal class IntLogPersistorImplTest {
    private val logPersistor: LogPersistor

    init {
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss")) ?: "UnknownTime"
        val fileHandler = FileHandler("./RFIDRefImplIntegrationTestLog-$dateTime.log")
        val logger = Logger.getLogger("IntegrationTest")
        logger.addHandler(fileHandler)
        logPersistor = LogPersistorImpl(logger)
    }

    @Test
    fun noMisplacedTagFound() {
        logPersistor.noMisplacedTagFound(listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")))
    }

    @Test
    fun misplacedTagFound() {
        logPersistor.misplacedTagFound(listOf(LibraryCopy("ZYXW1","1337","Book","Vertical")),
            listOf(LibraryCopy("ABCD1","1","Book","Horizontal"),LibraryCopy("ABCD2","1","Book","Horizontal"),LibraryCopy("ABCD3","1","Book","Horizontal")),
            ""
        )
    }
}
