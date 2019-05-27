package ch.bda.baumannwicki.tagreader.devicecommunication

import rfid.communication.HyientechAntennaPositions
import rfid.communicationid.TagInformation

interface CommunicationDriver {
    fun findAllRfids(): List<TagInformation>
    fun findAllRfids(timeout: Int): List<TagInformation>
    fun isSingleTagReachable(uid: TagInformation): Boolean
    fun switchToAntenna(hyientechAntennaPosition: HyientechAntennaPositions)
    fun readBlocks(from: Byte, numberOfBlocksToRead: Byte, tagInformation: TagInformation): List<Byte>
}
