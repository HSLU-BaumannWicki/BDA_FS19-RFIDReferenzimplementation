package ch.bda.baumannwicki.tagreader

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.tagreader.devicecommunication.CommunicationDriver
import ch.bda.baumannwicki.tagreader.devicecommunication.DeviceCommunicationException
import ch.bda.baumannwicki.tagreader.devicecommunication.hyientech.HyientechAntennaPosition
import rfid.communication.AntennaPosition
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
            var libraryCopyIDList: MutableList<LibraryCopyId> = readReachableTagsFromAntenna(HyientechAntennaPosition.ONE)
            libraryCopyIDList = readReachableTagsFromAntenna(HyientechAntennaPosition.TWO, libraryCopyIDList)
            communicationQueue.offer(libraryCopyIDList)
        }
    }

    private fun readReachableTagsFromAntenna(
        antennaPosition: AntennaPosition,
        libraryCopyList: MutableList<LibraryCopyId> = ArrayList()
    ): MutableList<LibraryCopyId> {
        communicationDriver.switchToAntenna(antennaPosition)
        var tagInformations: List<TagInformation> = findAllRFIDs()
        return getBlocksFromTagsToQueue(tagInformations, libraryCopyList)
    }

    private fun getBlocksFromTagsToQueue(
        tagInformations: List<TagInformation>,
        list: MutableList<LibraryCopyId>
    ): MutableList<LibraryCopyId> {
        for (tag: TagInformation in tagInformations) {
            try {
                val libraryCopyId = LibraryCopyId(readBlockFromTags(tag))
                list.add(libraryCopyId)
            } catch (exception: DeviceCommunicationException) {
                // swallow Exception cause the tag may got unreachable
            }
        }
        return list
    }

    private fun readBlockFromTags(tag: TagInformation) = communicationDriver.readBlocks(0, 4, tag)

    private fun findAllRFIDs() = communicationDriver.findAllRfids(readingTimeForInventory)

}
