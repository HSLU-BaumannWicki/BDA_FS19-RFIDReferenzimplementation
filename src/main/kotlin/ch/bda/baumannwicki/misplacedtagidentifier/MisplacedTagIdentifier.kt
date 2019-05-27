package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy

interface MisplacedTagIdentifier {
    fun getMisplacedTags(allTags: List<LibraryCopy>): List<LibraryCopy>
    fun getBackRelationBoxId(allTags: List<LibraryCopy>): String
}
