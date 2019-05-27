package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.interaction.ConsoleInteraction
import ch.bda.baumannwicki.ui.view.MessagesView
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean


class ConsoleUIMessagesController(
    private val messagesRecievingQueue: ConcurrentLinkedQueue<String>,
    private val run: AtomicBoolean,
    private val messagesView: MessagesView,
    private val consoleInteraction: ConsoleInteraction
) {

    fun runView() {
        while (run.get()) {
            if (!messagesRecievingQueue.isEmpty()) {
                val message: String = messagesRecievingQueue.poll() ?: ""
                messagesView.displayMessage(message)
            }
            val line = consoleInteraction.nextLine()
            if (line != "") run.set(!line.contains(Regex("^[qQ]")))
        }
        consoleInteraction.close()
    }
}
