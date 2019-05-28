package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.LibraryCopySupplier
import ch.bda.baumannwicki.misplacedtagidentifier.log.LogPersistor
import ch.bda.baumannwicki.uimessage.Message
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MisplacedTagIdentifyController(
    val run: AtomicBoolean,
    val incomingTagInformationQueue: Queue<List<LibraryCopyId>>,
    val misplacedTagIdentifier: MisplacedTagIdentifier,
    val libraryCopySupplier: LibraryCopySupplier,
    val logPersistor: LogPersistor,
    val messegesToViewQueue: Queue<Message>
) {

    fun runMisplacedTagRecognizerControllerTest() {
        while (run.get()) {
            if (!incomingTagInformationQueue.isEmpty()) {
                val libraryCopyList: MutableList<LibraryCopy> = ArrayList()
                val tagList: List<LibraryCopyId> = incomingTagInformationQueue.poll()
                for (tag: LibraryCopyId in tagList) {
                    libraryCopyList.add(libraryCopySupplier.getLibraryCopyByID(tag.toString()))
                }
                var misplacedTags: List<LibraryCopy> = emptyList()
                var box = ""

                try {
                    misplacedTags = misplacedTagIdentifier.getMisplacedTags(libraryCopyList)
                    box = misplacedTagIdentifier.getBackRelationBoxId(libraryCopyList)
                } catch (error: BoxIdentificationNotPossibleException) {
                    logPersistor.error(error)
                }
                if (misplacedTags.isNotEmpty()) {
                    logPersistor.misplacedTagFound(misplacedTags, libraryCopyList, box)
                    messegesToViewQueue.offer(Message("MisplacedTag Found (found in Box: \"$box\"): $misplacedTags"))
                } else {
                    logPersistor.noMisplacedTagFound(libraryCopyList)
                }
            }
        }
    }
}
