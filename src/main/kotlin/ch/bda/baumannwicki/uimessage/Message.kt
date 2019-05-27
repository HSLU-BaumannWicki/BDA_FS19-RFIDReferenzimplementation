package ch.bda.baumannwicki.uimessage

class Message(private val message: String = "") {
    override fun toString(): String {
        return message
    }

    override fun equals(other: Any?): Boolean {
        if (super.equals(other))
            return true
        if (other is Message)
            return other.message.equals(message)
        return false
    }
}