package ch.bda.baumannwicki.misplacedtagidentifier.log

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy

interface LogPersistor {
    fun misplacedTagFound(
        misplacedTag: List<LibraryCopy>,
        correctPlacedTags: List<LibraryCopy>,
        box: String
    )
    fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>)
    fun error(error: Throwable)
}
