package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import java.time.LocalDateTime
import java.util.logging.FileHandler
import java.util.logging.Logger

class LogPersistorImplementation : LogPersistor {
    private val dateTime = LocalDateTime.now()
    private val fileHandler: FileHandler = FileHandler("./RFIDRefImplTestLog-${dateTime.year}.${dateTime.monthValue}.${dateTime.dayOfMonth}-${dateTime.hour}.${dateTime.minute}.${dateTime.second}.log")
    private val logger = Logger.getLogger("RFIDRefImplLogger")

    init {
        logger.addHandler(fileHandler)
        logger.info("Logger startet at ${dateTime.hour}.${dateTime.minute}.${dateTime.second}")
    }

    override fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>) {
        logger.warning("No Misplaced Tag Found")
        correctPlacedTags.stream().forEach { libraryCopy -> logger.info("Copy found: ${libraryCopy.article}") }
    }

    override fun misplacedTagFound(misplacedTag: List<LibraryCopy>, correctPlacedTags: List<LibraryCopy>) {
        logger.warning("Misplaced Tag Found")
        misplacedTag.stream().forEach { libraryCopy -> logger.warning("Misplaced Tag found: ${libraryCopy.article}") }
        correctPlacedTags.stream().forEach { libraryCopy -> logger.info("Copy found: ${libraryCopy.article}") }
    }

}