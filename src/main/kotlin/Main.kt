import ch.bda.baumannwicki.data.parser.CsvLibraryCopyParser
import ch.bda.baumannwicki.data.supplier.LibraryCopyCSVSupplier
import ch.bda.baumannwicki.log.LogPersistorImpl
import ch.bda.baumannwicki.misplacedrecognizer.MisplacedRecognizerImpl
import ch.bda.baumannwicki.misplacedrecognizer.MisplacedTagRecognizerController
import ch.bda.baumannwicki.rfidcommunication.ContinuousReader
import ch.bda.baumannwicki.ui.ConsoleUIMessagesController
import ch.bda.baumannwicki.ui.view.MessageViewImpl
import ch.bda.baumannwicki.util.ConsoleInteraction
import rfid.communication.HyientechDeviceCommunicationDriver
import rfid.communicationid.TagInformation
import util.ReadableResourceFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.FileHandler
import java.util.logging.Logger

fun main() {
    // Log Persistor
    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss")) ?: "UnknownTime"
    val fileHandler = FileHandler("./RFIDRefImplIntegrationTestLog-$dateTime.log")
    val logger = Logger.getLogger("RFIDReferenzimplementation")
    logger.addHandler(fileHandler)
    val logPersistor = LogPersistorImpl(logger)

    // Continuous Reader
    val messegeQueueToView = ConcurrentLinkedQueue<String>()
    val run = AtomicBoolean(true)
    val tagInformationIncommintQueue = ConcurrentLinkedQueue<List<TagInformation>>()
    val misplacedRecognizer = MisplacedRecognizerImpl()
    val libraryCopySupplier =
        LibraryCopyCSVSupplier(ReadableResourceFile("Artikel_Behaelter.csv"), CsvLibraryCopyParser())
    val communicationDriver = HyientechDeviceCommunicationDriver("Basic")
    communicationDriver.initialize()

    val continuousReader = ContinuousReader(run, tagInformationIncommintQueue, 200, communicationDriver)

    val misplacedRecognizeController = MisplacedTagRecognizerController(
        run,
        tagInformationIncommintQueue,
        misplacedRecognizer,
        libraryCopySupplier,
        logPersistor,
        messegeQueueToView
    )

    val consoleInteraction = ConsoleInteraction(System.`in`)
    val messageView = MessageViewImpl()
    val ui = ConsoleUIMessagesController(messegeQueueToView, run, messageView, consoleInteraction)

    val thread = Thread { misplacedRecognizeController.runMisplacedTagRecognizerControllerTest() }
    val thread2 = Thread { continuousReader.readContinuouslyForNewRFIDTags() }
    thread2.start()
    thread.start()

    ui.runView()

    thread.join(10000)
    thread2.join(10000)
}
