package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy
import ch.bda.baumannwicki.misplacedrecognizer.BoxIdentificationNotPossibleException

interface LogPersistor {
    fun misplacedTagFound(
        misplacedTag: List<LibraryCopy>,
        correctPlacedTags: List<LibraryCopy>,
        box: String
    )
    fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>)
    fun error(error: Throwable)
}