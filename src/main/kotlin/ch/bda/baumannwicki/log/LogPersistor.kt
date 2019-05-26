package ch.bda.baumannwicki.log

import ch.bda.baumannwicki.data.LibraryCopy

interface LogPersistor {
    fun misplacedTagFound(misplacedTag: List<LibraryCopy>, correctPlacedTags: List<LibraryCopy>)
    fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>)
}