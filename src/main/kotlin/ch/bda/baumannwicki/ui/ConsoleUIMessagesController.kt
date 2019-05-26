package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessagesView
import java.io.BufferedReader
import java.util.concurrent.ConcurrentLinkedQueue


class ConsoleUIMessagesController(
    val messagesRecievingQueue: ConcurrentLinkedQueue<String>,
    val messagesSendingQueue: ConcurrentLinkedQueue<QueueMessages>,
    val messagesView: MessagesView,
    val bufferedReader: BufferedReader
) {
    fun runView() {
        //var br = BufferedReader(InputStreamReader(System.`in`, Charset.forName("ISO-8859-1")), 1024)
        var run = true
        while (run) {
            if (!messagesRecievingQueue.isEmpty()) {
                val message: String = messagesRecievingQueue.poll() ?: ""
                messagesView.displayMessage(message)
            }
            val line = bufferedReader.readLine()
            if (!line.isEmpty()) {
                //val readLinesFiltred = line.stream().filter { it.contains(Regex("^[qQ]")) }.count()
                run = !line.contains(Regex("^[qQ]"))
            }
        }
        bufferedReader.close()
        messagesSendingQueue.offer(QueueMessages.STOP_APPLICATION)
    }
}