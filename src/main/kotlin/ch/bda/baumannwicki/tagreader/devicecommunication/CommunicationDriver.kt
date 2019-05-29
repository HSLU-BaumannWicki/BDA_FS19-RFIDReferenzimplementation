package ch.bda.baumannwicki.tagreader.devicecommunication

import rfid.communication.AntennaPosition
import rfid.communicationid.TagInformation

interface CommunicationDriver {
    fun findAllRfids(): List<TagInformation>
    fun findAllRfids(timeout: Int): List<TagInformation>
    fun isSingleTagReachable(tagInformation: TagInformation): Boolean
    fun switchToAntenna(antennaPosition: AntennaPosition)
    fun readBlocks(from: Byte, numberOfBlocksToRead: Byte, tagInformation: TagInformation): List<Byte>
}
