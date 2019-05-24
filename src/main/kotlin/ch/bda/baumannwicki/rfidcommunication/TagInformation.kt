package rfid.communicationid

class TagInformation(val uid: List<Byte>) {
    override fun toString(): String {

        return uid.joinToString { myUid -> "%02X".format(myUid) }
    }

    companion object {
        open fun getByteListForHexString(hex: String): List<Byte> {
            val hex2 = hex.filter { char -> "[A-F0-9]".toRegex().containsMatchIn(char.toString()) }
            val tagId = ArrayList<Byte>()
            for (i in 0..7) {
                val myString = hex2
                tagId.add(myString.substring(i * 2, i * 2 + 2).toLong(radix = 16).toByte())
            }
            return tagId
        }
    }
}
