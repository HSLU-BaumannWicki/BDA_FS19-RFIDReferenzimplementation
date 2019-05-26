package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessageViewImpl
import org.junit.jupiter.api.Test
import java.io.StringReader
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertEquals

internal class ConsoleUIMessagesControllerTest {

    @Test
    fun givenReaderContainingQ_whenCallingRunView_thenQueueShouldContainStopApplication() {
        val reader = StringReader("q\n")
        val queue = ConcurrentLinkedQueue<QueueMessages>()
        val testee = ConsoleUIMessagesController(
            ConcurrentLinkedQueue(),
            queue,
            MessageViewImpl(),
            reader
        )
        testee.runView()
        assertEquals(QueueMessages.STOP_APPLICATION, queue.poll())
    }

    @Test
    fun givenMessageQueueContainingTest_whenCallingRunView_thenMessageViewShouldDisplayTest() {
        val reader = StringReader("q\n")
        val queue = ConcurrentLinkedQueue<String>()
        queue.offer("Test")
        val messageVieStub = MessageViewStub()
        val testee = ConsoleUIMessagesController(
            queue,
            ConcurrentLinkedQueue(),
            messageVieStub,
            reader
        )
        testee.runView()
        assertEquals(listOf("Test"), messageVieStub.displayedMessages)
    }
}