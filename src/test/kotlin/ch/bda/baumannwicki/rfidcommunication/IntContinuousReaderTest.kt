package ch.bda.baumannwicki.rfidcommunication

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import rfid.communication.HyientechDeviceCommunicationDriver
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

internal class IntContinuousReaderTest {
    @Disabled
    @Test
    fun readContinuouslyForNewRFIDTags() {
        val communicationDriver = HyientechDeviceCommunicationDriver("Basic")
        communicationDriver.initialize()
        val communicationQueue = ConcurrentLinkedQueue<List<Byte>>()
        val keepRunning = AtomicBoolean(true)
        val reader =
            ContinuousReader(keepRunning, communicationQueue, 100, communicationDriver)
        Thread {
            reader.readContinuouslyForNewRFIDTags()
        }.start()
        Thread.sleep(10000)
        keepRunning.set(false)
        Thread.sleep(100)
        for (entries in communicationQueue) {
            var text = ""
            for (entrie in entries) {
                if (entrie in 48..122)
                    text += entrie.toChar()
            }
            if(text != "") System.out.println(text)
        }
    }
}
