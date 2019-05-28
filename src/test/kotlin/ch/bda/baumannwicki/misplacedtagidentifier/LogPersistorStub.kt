package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import ch.bda.baumannwicki.misplacedtagidentifier.log.LogPersistor

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
