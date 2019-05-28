package ch.bda.baumannwicki.ui.view

import ch.bda.baumannwicki.uimessage.Message

class MessageViewImpl : MessagesView {
    override fun displayMessage(message: Message) {
        println(message.toString())
    }
}
