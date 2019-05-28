package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessagesView
import ch.bda.baumannwicki.uimessage.Message

class MessageViewStub : MessagesView {
    val displayedMessages = ArrayList<Message>()
    override fun displayMessage(message: Message) {
        displayedMessages.add(message)
    }

}
