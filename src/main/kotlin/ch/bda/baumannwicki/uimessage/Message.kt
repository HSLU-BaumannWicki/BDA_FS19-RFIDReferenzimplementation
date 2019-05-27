package ch.bda.baumannwicki.uimessage

class Message(private val message: String = "") {
    override fun toString(): String {
        return message
    }
}