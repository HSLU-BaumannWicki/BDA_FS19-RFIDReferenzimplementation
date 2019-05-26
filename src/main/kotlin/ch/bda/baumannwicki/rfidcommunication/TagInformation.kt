package rfid.communicationid

class TagInformation(val uid: List<Byte>) {
    override fun toString(): String {
        return "[" + uid.joinToString { myUid -> "%02X".format(myUid) } + "]"
    }

    fun toASCIIString(): String {
        var text = ""
        for (entrie in uid.reversed()) {
            if (entrie in 48..122)
                text += entrie.toChar()
        }
        return text
    }
}
