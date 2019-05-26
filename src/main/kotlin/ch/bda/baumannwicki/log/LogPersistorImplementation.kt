package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import java.time.LocalDateTime
import java.util.logging.FileHandler
import java.util.logging.Logger

class LogPersistorImplementation(fileHandler: FileHandler, val logger: Logger) : LogPersistor {
    init {
        val dateTime = LocalDateTime.now()
        logger.addHandler(fileHandler)
        logger.info("Logger startet at ${dateTime.hour}.${dateTime.minute}.${dateTime.second}")
    }

    override fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>) {
        logger.info("No Misplaced Tag Found")
        logger.info("MisplacedTag found: " + correctPlacedTags.map(this::mapLibraryCopyToString))
    }

    override fun misplacedTagFound(misplacedTag: List<LibraryCopy>, correctPlacedTags: List<LibraryCopy>) {
        logger.info("MisplacedTag found: " + misplacedTag.map(this::mapLibraryCopyToString))
        logger.info("LibraryCopies found: " + correctPlacedTags.map(this::mapLibraryCopyToString))
    }

    private fun mapLibraryCopyToString(libraryCopy: LibraryCopy): String {
        return "ArticleID: ${libraryCopy.article}, BoxID: ${libraryCopy.box}"
    }
}