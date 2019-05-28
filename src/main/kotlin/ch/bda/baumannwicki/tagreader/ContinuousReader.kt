package ch.bda.baumannwicki.tagreader

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.tagreader.devicecommunication.CommunicationDriver
import ch.bda.baumannwicki.tagreader.devicecommunication.DeviceCommunicationException
import rfid.communicationid.TagInformation
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class ContinuousReader(
    val continueRunning: AtomicBoolean,
    val communicationQueue: ConcurrentLinkedQueue<List<LibraryCopyId>>,
    val readingTimeForInventory: Int,
    val communicationDriver: CommunicationDriver
) {
    fun readContinuouslyForNewRFIDTags() {
        while (continueRunning.get()) {
            var tagInformations: List<TagInformation> = findAllRFIDs()
            sendBlocksFromTagsToQueue(tagInformations)
        }
    }

    private fun sendBlocksFromTagsToQueue(tagInformations: List<TagInformation>) {
        val list: MutableList<LibraryCopyId> = ArrayList()
        for (tag: TagInformation in tagInformations) {
            try {
                val libraryCopyId = LibraryCopyId(readBlockFromTags(tag))
                list.add(libraryCopyId)
            } catch (exception: DeviceCommunicationException) {
                // swallow Exception cause the tag may got unreachable
            }
        }
        communicationQueue.offer(list)
    }

    private fun readBlockFromTags(tag: TagInformation) = communicationDriver.readBlocks(0, 4, tag)

    private fun findAllRFIDs() = communicationDriver.findAllRfids(readingTimeForInventory)

}
