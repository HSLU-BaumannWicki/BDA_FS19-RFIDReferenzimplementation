package ch.bda.baumannwicki.tagreader.devicecommunication.hyientech

import rfid.communication.AntennaPosition

enum class HyientechAntennaPosition(val antennaPosition: Int): AntennaPosition {
    ONE(1), TWO(2), THREE(4), FOUR(8);
    override fun getPositionAsInt(): Int{
        return antennaPosition
    }
}