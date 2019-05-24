package ch.bda.baumannwicki.rfidcommunication

import rfid.communication.CommunicationDriver
import rfid.communicationid.TagInformation
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class ContinuousReader(
    val continueRunning: AtomicBoolean,
    val communicationQueue: ConcurrentLinkedQueue<List<Byte>>,
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
        for (tag in tagInformations) {
            try {
                val byteList:List<Byte> = readBlockFromTags(tag)
                communicationQueue.offer(byteList)
            } catch (exception: DeviceCommunicationException) {
                // swallow Exception cause the tag may got unreachable
            }
        }
    }

    private fun readBlockFromTags(tag: TagInformation) = communicationDriver.readBlocks(0, 4, tag)

    private fun findAllRFIDs() = communicationDriver.findAllRfids(readingTimeForInventory)

}
