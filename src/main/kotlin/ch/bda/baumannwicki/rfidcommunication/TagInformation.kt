package rfid.communicationid

class TagInformation(val uid: List<Byte>) {
    override fun toString(): String {
        return "[" + uid.joinToString { myUid -> "%02X".format(myUid) } + "]"
    }
}
