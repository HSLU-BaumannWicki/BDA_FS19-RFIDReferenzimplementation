package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.parser.CsvLibraryCopyParser
import ch.bda.baumannwicki.data.supplier.LibraryCopyCSVSupplier
import ch.bda.baumannwicki.data.supplier.LibraryCopySupplier
import ch.bda.baumannwicki.rfidcommunication.ContinuousReader
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import rfid.communication.HyientechDeviceCommunicationDriver
import rfid.communicationid.TagInformation
import util.ReadableFileStub
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class IntMisplacedTagRecognizerControllerTest() {
    @Disabled
    @Test
    fun IntRunMisplacedTagRecognizerControllerTest() {

        val run = AtomicBoolean(true)
        val tagInformationIncommintQueue = ConcurrentLinkedQueue<List<TagInformation>>()
        val misplacedRecognizer: MisplacedRecognizer = MisplacedRecognizerImpl()
        val libraryCopySupplier: LibraryCopySupplier =
            LibraryCopyCSVSupplier(ReadableFileStub(""), CsvLibraryCopyParser())
        val logPersistor = LogPersistorStub()

        val communicationDriver = HyientechDeviceCommunicationDriver("Basic")
        communicationDriver.initialize()

        val reader = ContinuousReader(run, tagInformationIncommintQueue, 200, communicationDriver)

        val testee = MisplacedTagRecognizerController(
            run,
            tagInformationIncommintQueue,
            misplacedRecognizer,
            libraryCopySupplier,
            logPersistor
        )
        val thread = Thread { testee.runMisplacedTagRecognizerControllerTest() }
        val thread2 = Thread { reader.readContinuouslyForNewRFIDTags() }
        thread2.run()
        thread.run()
        Thread.sleep(10000)
        run.set(false)
        thread.join(10000)
        thread2.join(10000)
        println("${logPersistor.listAllTags} ..... ${logPersistor.listMisplaced}")
    }
}