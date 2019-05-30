package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.misplacedtagidentifier.data.parser.CsvLibraryCopyParser
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.LibraryCopyCSVSupplier
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.LibraryCopySupplier
import ch.bda.baumannwicki.tagreader.ContinuousReader
import ch.bda.baumannwicki.uimessage.Message
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import rfid.communication.HyientechDeviceCommunicationDriver
import util.ReadableFileStub
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class IntMisplacedTagIdentifyControllerTest() {
    @Disabled
    @Test
    fun IntRunMisplacedTagRecognizerControllerTest() {

        val run = AtomicBoolean(true)
        val tagInformationIncommintQueue = ConcurrentLinkedQueue<List<LibraryCopyId>>()
        val misplacedTagIdentifier: MisplacedTagIdentifier =
            MisplacedTagIdentifierImpl()
        val libraryCopySupplier: LibraryCopySupplier =
            LibraryCopyCSVSupplier(
                ReadableFileStub("IDANEPASE1337,123,f,2,\nIDANEPASE1338,123,f,2,\nIDANEPASE1339,124,f,2,"),
                CsvLibraryCopyParser()
            )
        val logPersistor = LogPersistorStub()

        val communicationDriver = HyientechDeviceCommunicationDriver("Basic")
        communicationDriver.initialize()

        val reader = ContinuousReader(tagInformationIncommintQueue, 200, communicationDriver)

        val testee = MisplacedTagIdentifyController(
            tagInformationIncommintQueue,
            misplacedTagIdentifier,
            libraryCopySupplier,
            logPersistor,
            ConcurrentLinkedQueue<Message>()
        )

        val thread = Thread {
            while (run.get()) {
                testee.runMisplacedTagRecognizerControllerTest()
            }
        }
        val thread2 = Thread {
            while (run.get()) {
                reader.readNewRFIDTags()
            }
        }
        thread2.start()
        thread.start()
        Thread.sleep(10000)
        run.set(false)
        thread.join(10000)
        thread2.join(10000)
        println("${logPersistor.listAllTags}\n${logPersistor.listMisplaced}")
    }
}
