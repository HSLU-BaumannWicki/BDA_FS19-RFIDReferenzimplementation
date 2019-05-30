package ch.bda.baumannwicki.tagreaderTagInformation

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.tagreader.ContinuousReader
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
        val communicationQueue = ConcurrentLinkedQueue<List<LibraryCopyId>>()
        val keepRunning = AtomicBoolean(true)
        val reader =
            ContinuousReader(communicationQueue, 100, communicationDriver)
        Thread {
            while (keepRunning.get()) {
                reader.readNewRFIDTags()
            }
        }.start()
        Thread.sleep(10000)
        keepRunning.set(false)
        Thread.sleep(100)
        for (tags: List<LibraryCopyId> in communicationQueue) {
            for (tag: LibraryCopyId in tags) {
                var text: String = tag.toString()
                if (text != "") println(text)
            }
        }
    }
}
