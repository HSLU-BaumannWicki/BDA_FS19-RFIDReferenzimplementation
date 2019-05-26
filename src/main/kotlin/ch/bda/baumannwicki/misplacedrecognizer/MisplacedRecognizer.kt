package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy

interface MisplacedRecognizer {
    fun getMisplacedTags(allTags: List<LibraryCopy>): List<LibraryCopy>
    fun getBackRelationBoxId(allTags: List<LibraryCopy>): String
}
