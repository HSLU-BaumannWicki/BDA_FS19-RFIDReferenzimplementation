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
    val logPersistor: LogPersistor,
    val messegesToViewQueue: Queue<String>
) {

    fun runMisplacedTagRecognizerControllerTest() {
        while (run.get()) {
            if (!incomingTagInformationQueue.isEmpty()) {
                val libraryCopyList: MutableList<LibraryCopy> = ArrayList()
                val tagList: List<TagInformation> = incomingTagInformationQueue.poll()
                for (tag: TagInformation in tagList) {
                    libraryCopyList.add(libraryCopySupplier.getLibraryCopyByID(tag.toASCIIString()))
                }
                var misplacedTags: List<LibraryCopy> = emptyList()
                var box = ""

                try {
                    misplacedTags = misplacedRecognizer.getMisplacedTags(libraryCopyList)
                    box = misplacedRecognizer.getBackRelationBoxId(libraryCopyList)
                } catch (error: BoxIdentificationNotPossibleException) {
                    logPersistor.error(error)
                }
                if (misplacedTags.isNotEmpty()) {
                    logPersistor.misplacedTagFound(misplacedTags, libraryCopyList, box)
                    messegesToViewQueue.offer("MisplacedTag Found (should be in Box $box): $misplacedTags")
                } else {
                    logPersistor.noMisplacedTagFound(libraryCopyList)
                }
            }
        }
    }
}
