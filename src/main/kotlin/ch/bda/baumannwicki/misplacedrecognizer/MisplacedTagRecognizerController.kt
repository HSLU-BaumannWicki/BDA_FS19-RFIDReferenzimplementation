package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy
import ch.bda.baumannwicki.data.supplier.LibraryCopySupplier
import ch.bda.baumannwicki.log.LogPersistor
import rfid.communicationid.TagInformation
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MisplacedTagRecognizerController(
    val run: AtomicBoolean,
    val incomingTagInformationQueue: Queue<List<TagInformation>>,
    val misplacedRecognizer: MisplacedRecognizer,
    val libraryCopySupplier: LibraryCopySupplier,
    val logPersistor: LogPersistor
) {

    fun runMisplacedTagRecognizerControllerTest() {
        while (run.get()) {
            if (!incomingTagInformationQueue.isEmpty()) {
                val libraryCopyList: MutableList<LibraryCopy> = ArrayList()
                val tagList: List<TagInformation> = incomingTagInformationQueue.poll()
                for (tag: TagInformation in tagList) {
                    libraryCopyList.add(libraryCopySupplier.getLibraryCopyByID(tag.toASCIIString()))
                }
                val misplacedTags: List<LibraryCopy> = misplacedRecognizer.getMisplacedTags(libraryCopyList)
                if (!misplacedTags.isEmpty()) {
                    logPersistor.misplacedTagFound(misplacedTags, libraryCopyList)
                } else {
                    logPersistor.noMisplacedTagFound(libraryCopyList)
                }
            }
        }
    }
}
