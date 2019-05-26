package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessagesView
import ch.bda.baumannwicki.util.ConsoleInteraction
import java.util.concurrent.ConcurrentLinkedQueue


class ConsoleUIMessagesController(
    val messagesRecievingQueue: ConcurrentLinkedQueue<String>,
    val messagesSendingQueue: ConcurrentLinkedQueue<QueueMessages>,
    val messagesView: MessagesView,
    val consoleInteraction: ConsoleInteraction
) {

    fun runView() {
        var run = true
        while (run) {
            if (!messagesRecievingQueue.isEmpty()) {
                val message: String = messagesRecievingQueue.poll() ?: ""
                messagesView.displayMessage(message)
            }
            val line = consoleInteraction.nextLine()
            run = !line.contains(Regex("^[qQ]"))
        }
        consoleInteraction.close()
        messagesSendingQueue.offer(QueueMessages.STOP_APPLICATION)
    }
}