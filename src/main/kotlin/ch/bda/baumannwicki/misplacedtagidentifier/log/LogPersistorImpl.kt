package ch.bda.baumannwicki.misplacedtagidentifier.log

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Logger

class LogPersistorImpl(private val logger: Logger) : LogPersistor {

    init {
        val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) ?: ""
        logger.info("Logger startet at $dateTime")
    }

    override fun error(error: Throwable) {
        logger.warning(error.toString())
    }

    override fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>) = printTagsFound(correctPlacedTags)


    override fun misplacedTagFound(
        misplacedTag: List<LibraryCopy>,
        correctPlacedTags: List<LibraryCopy>,
        box: String
    ) {
        misplacedTagFound(misplacedTag, box)
        noMisplacedTagFound(correctPlacedTags)
    }

    private fun printTagsFound(tags: List<LibraryCopy>) {
        if (!tags.isEmpty()) logger.info(LIBRARY_TAGS_FOUND + tags.map(this::mapLibraryCopyToString))
    }

    private fun misplacedTagFound(tags: List<LibraryCopy>, box: String) {
        if (!tags.isEmpty()) logger.warning("$MISPLACED_TAGS_FOUND $box  -> " + tags.map(this::mapLibraryCopyToString))
    }

    private fun mapLibraryCopyToString(libraryCopy: LibraryCopy): String {
        return "ArticleID: ${libraryCopy.article}, BoxID: ${libraryCopy.box}"
    }

    companion object {
        val LIBRARY_TAGS_FOUND = "LibraryCopies found: "
        val MISPLACED_TAGS_FOUND = "MisplacedTag found, Tags are wrongfully placed in Box"
    }
}
