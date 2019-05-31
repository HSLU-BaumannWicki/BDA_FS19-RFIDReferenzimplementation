package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.LibraryCopySupplier
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.NoLibraryCopyFoundException
import ch.bda.baumannwicki.misplacedtagidentifier.log.LogPersistor
import ch.bda.baumannwicki.uimessage.Message
import java.util.*
import kotlin.collections.HashSet

class MisplacedTagIdentifyController(
    val incomingTagInformationQueue: Queue<List<LibraryCopyId>>,
    val misplacedTagIdentifier: MisplacedTagIdentifier,
    val libraryCopySupplier: LibraryCopySupplier,
    val logPersistor: LogPersistor,
    val messegesToViewQueue: Queue<Message>
) {

    fun runMisplacedTagRecognizerControllerTest() {
        if (!incomingTagInformationQueue.isEmpty()) {
            val libraryCopyList: MutableSet<LibraryCopy> = HashSet()
            val tagList: List<LibraryCopyId> = incomingTagInformationQueue.poll()
            for (tag: LibraryCopyId in tagList) {
                addExistingLibraryCopyIdToSet(tag, libraryCopyList)
            }
            var misplacedTags: List<LibraryCopy> = emptyList()
            var box = ""

            try {
                misplacedTags = misplacedTagIdentifier.getMisplacedTags(libraryCopyList.toList())
                box = misplacedTagIdentifier.getBackRelationBoxId(libraryCopyList.toList())
            } catch (error: BoxIdentificationNotPossibleException) {
                logPersistor.error(error)
            }
            if (misplacedTags.isNotEmpty()) {
                logPersistor.misplacedTagFound(misplacedTags, libraryCopyList.toList(), box)
                messegesToViewQueue.offer(Message("MisplacedTag Found (found in Box: \"$box\"): $misplacedTags"))
            } else {
                logPersistor.noMisplacedTagFound(libraryCopyList.toList())
                sendMessageToView(libraryCopyList, box)
            }
        }
    }

    private fun addExistingLibraryCopyIdToSet(tag: LibraryCopyId, libraryCopyList: MutableSet<LibraryCopy>) {
        try {
            if (tag.toString() != "") libraryCopyList.add(libraryCopySupplier.getLibraryCopyByID(tag.toString()))
        } catch (exception: NoLibraryCopyFoundException) {
            logPersistor.error(exception)
        }
    }

    private fun sendMessageToView(
        libraryCopyList: MutableSet<LibraryCopy>,
        box: String
    ) {
        if (libraryCopyList.isNotEmpty()) {
            messegesToViewQueue.offer(
                Message(
                    "Box: $box, NumberOfTags: ${libraryCopyList.count()} ${libraryCopyList.map(
                        LibraryCopy::article
                    ).toList().toString()}."
                )
            )
        }
    }
}
