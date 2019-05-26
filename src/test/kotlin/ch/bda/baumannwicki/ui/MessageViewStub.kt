package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessagesView

class MessageViewStub : MessagesView {
    val displayedMessages = ArrayList<String>()
    override fun displayMessage(message: String) {
        displayedMessages.add(message)
    }

}
