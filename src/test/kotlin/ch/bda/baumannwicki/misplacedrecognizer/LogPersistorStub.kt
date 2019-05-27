package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy
import ch.bda.baumannwicki.log.LogPersistor

class LogPersistorStub : LogPersistor {

    val listMisplaced = ArrayList<String>()
    val listAllTags = ArrayList<String>()
    override fun misplacedTagFound(
        misplacedTag: List<LibraryCopy>,
        correctPlacedTags: List<LibraryCopy>,
        box: String
    ) {
        listMisplaced.addAll(misplacedTag.map { it.toString() })
        listAllTags.addAll(correctPlacedTags.map { it.toString() })
    }
    override fun error(error: Throwable) {
    }

    override fun noMisplacedTagFound(correctPlacedTags: List<LibraryCopy>) {
        listAllTags.addAll(correctPlacedTags.map { it.toString() })
    }
}
