package ch.bda.baumannwicki.ui.view

import ch.bda.baumannwicki.uimessage.Message

interface MessagesView {
    fun displayMessage(message: Message)
}
