package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessagesView
import java.io.Reader
import java.util.concurrent.ConcurrentLinkedQueue


class ConsoleUIMessagesController(
    val messagesRecievingQueue: ConcurrentLinkedQueue<String>,
    val messagesSendingQueue: ConcurrentLinkedQueue<QueueMessages>,
    val messagesView: MessagesView,
    val bufferedReader: Reader
) {
    fun runView() {
        //var br = BufferedReader(InputStreamReader(System.`in`, Charset.forName("ISO-8859-1")), 1024)
        var run = true
        while (run) {
            if (!messagesRecievingQueue.isEmpty()) {
                val message: String = messagesRecievingQueue.poll() ?: ""
                messagesView.displayMessage(message)
            }
            val readLines = bufferedReader.readLines()
            if (!readLines.isEmpty()) {
                val readLinesFiltred = readLines.stream().filter { it.contains(Regex("^[qQ]")) }.count()
                run = readLinesFiltred == 0L
            }
        }
        bufferedReader.close()
        messagesSendingQueue.offer(QueueMessages.STOP_APPLICATION)
    }
}