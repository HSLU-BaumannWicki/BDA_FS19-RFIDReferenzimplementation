package ch.bda.baumannwicki.rfidcommunication

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import rfid.communication.HyientechDeviceCommunicationDriver
import rfid.communicationid.TagInformation
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

internal class IntContinuousReaderTest {
    @Disabled
    @Test
    fun readContinuouslyForNewRFIDTags() {
        val communicationDriver = HyientechDeviceCommunicationDriver("Basic")
        communicationDriver.initialize()
        val communicationQueue = ConcurrentLinkedQueue<List<TagInformation>>()
        val keepRunning = AtomicBoolean(true)
        val reader =
            ContinuousReader(keepRunning, communicationQueue, 100, communicationDriver)
        Thread {
            reader.readContinuouslyForNewRFIDTags()
        }.start()
        Thread.sleep(10000)
        keepRunning.set(false)
        Thread.sleep(100)
        for (tags in communicationQueue) {
            for (tag in tags) {
                var text = tag.toASCIIString()
                if (text != "") println(text)
            }
        }
    }
}
