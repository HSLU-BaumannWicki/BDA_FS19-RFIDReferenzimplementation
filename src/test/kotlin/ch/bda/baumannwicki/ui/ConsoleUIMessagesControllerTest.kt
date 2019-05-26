package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessageViewImpl
import org.junit.jupiter.api.Test
import java.io.BufferedReader
import java.io.StringReader
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertEquals

internal class ConsoleUIMessagesControllerTest {

    @Test
    fun givenReaderContainingQ_whenCallingRunView_thenQueueShouldContainStopApplication() {
        // arrange
        val reader = BufferedReader(StringReader("q\n"))
        val queue = ConcurrentLinkedQueue<QueueMessages>()
        val testee = ConsoleUIMessagesController(ConcurrentLinkedQueue(), queue, MessageViewImpl(), reader)
        // act
        testee.runView()
        // assert
        assertEquals(QueueMessages.STOP_APPLICATION, queue.poll())
    }

    @Test
    fun givenMessageQueueContainingTest_whenCallingRunView_thenMessageViewShouldDisplayTest() {
        // arrange
        val reader = BufferedReader(StringReader("q\n"))
        val queue = ConcurrentLinkedQueue<String>(listOf("Test"))
        val messageVieStub = MessageViewStub()
        val testee = ConsoleUIMessagesController(queue, ConcurrentLinkedQueue(), messageVieStub, reader)
        // act
        testee.runView()
        // assert
        assertEquals(listOf("Test"), messageVieStub.displayedMessages)
    }

    @Test
    fun givenMessageQueueContainingTestandtest_whenCallingRunViewAsyncronous_thenMessageViewShouldDisplayTestandtest() {
        // arrange
        val reader = object : BufferedReader(StringReader("")) {
            var calls = 5
            override fun readLine(): String {
                return if (calls-- > 0) "wayne" else "q"
            }
        }
        val queue = ConcurrentLinkedQueue<String>(listOf("Test", "test"))
        val messageVieStub = MessageViewStub()
        val testee = ConsoleUIMessagesController(queue, ConcurrentLinkedQueue(), messageVieStub, reader)
        // act
        testee.runView()
        // assert
        assertEquals(listOf("Test", "test"), messageVieStub.displayedMessages)
    }
}