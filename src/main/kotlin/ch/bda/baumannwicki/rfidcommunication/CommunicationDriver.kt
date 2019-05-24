package rfid.communication

import rfid.communicationid.TagInformation

interface CommunicationDriver {
    fun getAllRfids(): List<TagInformation>
    fun getAllRfids(timeout: Int): List<TagInformation>
    fun isSingleTagReachable(uid: TagInformation): Boolean
    fun switchToAntenna(hyientechAntennaPosition: HyientechAntennaPositions)
}