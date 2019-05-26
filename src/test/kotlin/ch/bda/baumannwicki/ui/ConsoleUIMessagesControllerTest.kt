package ch.bda.baumannwicki.ui

import ch.bda.baumannwicki.ui.view.MessageViewImpl
import ch.bda.baumannwicki.util.ConsoleInteraction
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.test.assertEquals

internal class ConsoleUIMessagesControllerTest {

    @Test
    fun givenReaderContainingQ_whenCallingRunView_thenQueueShouldContainStopApplication() {
        // arrange
        val consoleInteraction = ConsoleInteraction(ByteArrayInputStream("q\n".toByteArray()))
        val queue = ConcurrentLinkedQueue<QueueMessages>()
        val testee = ConsoleUIMessagesController(ConcurrentLinkedQueue(), queue, MessageViewImpl(), consoleInteraction)
        // act
        testee.runView()
        // assert
        assertEquals(QueueMessages.STOP_APPLICATION, queue.poll())
    }

    @Test
    fun givenMessageQueueContainingTest_whenCallingRunView_thenMessageViewShouldDisplayTest() {
        // arrange
        val consoleInteraction = ConsoleInteraction(ByteArrayInputStream("q\n".toByteArray()))
        val queue = ConcurrentLinkedQueue<String>(listOf("Test"))
        val messageVieStub = MessageViewStub()
        val testee = ConsoleUIMessagesController(queue, ConcurrentLinkedQueue(), messageVieStub, consoleInteraction)
        // act
        testee.runView()
        // assert
        assertEquals(listOf("Test"), messageVieStub.displayedMessages)
    }

    @Test
    fun givenMessageQueueContainingTestandtest_whenCallingRunView_thenMessageViewShouldDisplayTestandtest() {
        // arrange
        val consoleInteraction = object : ConsoleInteraction(ByteArrayInputStream("".toByteArray())) {
            var calls = 5
            override fun nextLine(): String {
                return if (calls-- > 0) "wayne" else "q"
            }
        }
        val queue = ConcurrentLinkedQueue<String>(listOf("Test", "test"))
        val messageVieStub = MessageViewStub()
        val testee = ConsoleUIMessagesController(queue, ConcurrentLinkedQueue(), messageVieStub, consoleInteraction)
        // act
        testee.runView()
        // assert
        assertEquals(listOf("Test", "test"), messageVieStub.displayedMessages)
    }

    @Disabled
    @Test
    fun int_givenMessageQueueContainingTestandtest_whenCallingRunView_thenMessageShouldGetDisplayedOnConsole() {
        println("Hello, World!")
        val queue = ConcurrentLinkedQueue<String>(listOf("Test", "test", "Test"))
        val testee =
            ConsoleUIMessagesController(
                queue,
                ConcurrentLinkedQueue(),
                MessageViewImpl(),
                ConsoleInteraction(System.`in`)
            )
        testee.runView()
    }
}