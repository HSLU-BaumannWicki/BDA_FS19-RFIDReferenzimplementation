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
            var tagInformations: List<TagInformation> = communicationDriver.findAllRfids(readingTimeForInventory)
            for (tag in tagInformations) {
                val byteList = communicationDriver.readBlocks(0, 2, tag)
                communicationQueue.offer(byteList)
            }
        }
    }

}
