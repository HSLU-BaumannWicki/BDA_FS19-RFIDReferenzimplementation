package ch.bda.baumannwicki

import ch.bda.baumannwicki.bookinformation.LibraryCopyId
import ch.bda.baumannwicki.misplacedtagidentifier.MisplacedTagIdentifierImpl
import ch.bda.baumannwicki.misplacedtagidentifier.MisplacedTagIdentifyController
import ch.bda.baumannwicki.misplacedtagidentifier.data.parser.CsvLibraryCopyParser
import ch.bda.baumannwicki.misplacedtagidentifier.data.supplier.LibraryCopyCSVSupplier
import ch.bda.baumannwicki.misplacedtagidentifier.log.LogPersistorImpl
import ch.bda.baumannwicki.tagreader.ContinuousReader
import ch.bda.baumannwicki.ui.ConsoleUIMessagesController
import ch.bda.baumannwicki.ui.interaction.ConsoleInteraction
import ch.bda.baumannwicki.ui.view.MessageViewImpl
import ch.bda.baumannwicki.uimessage.Message
import rfid.communication.HyientechDeviceCommunicationDriver
import util.ReadableResourceFile
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.logging.FileHandler
import java.util.logging.Logger
import kotlin.reflect.KFunction0

fun main() {
    // Log Persistor
    val dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss")) ?: "UnknownTime"
    val fileHandler = FileHandler("./RFIDRefImplIntegrationTestLog-$dateTime.log")
    val logger = Logger.getLogger("RFIDReferenzimplementation")
    logger.useParentHandlers = false
    for (handler in logger.handlers) {
        logger.removeHandler(handler)
    }
    logger.addHandler(fileHandler)
    val logPersistor = LogPersistorImpl(logger)

    // Continuous Reader
    val messegeQueueToView = ConcurrentLinkedQueue<Message>()
    val run = AtomicBoolean(true)
    val tagInformationIncommintQueue = ConcurrentLinkedQueue<List<LibraryCopyId>>()
    val misplacedRecognizer = MisplacedTagIdentifierImpl()
    val libraryCopySupplier =
        LibraryCopyCSVSupplier(ReadableResourceFile("/artikelBehaelter.csv"), CsvLibraryCopyParser())
    val communicationDriver = HyientechDeviceCommunicationDriver("/Basic.dll")
    communicationDriver.initialize()

    val continuousReader = ContinuousReader(tagInformationIncommintQueue, 200, communicationDriver)

    val misplacedRecognizeController = MisplacedTagIdentifyController(
        tagInformationIncommintQueue,
        misplacedRecognizer,
        libraryCopySupplier,
        logPersistor,
        messegeQueueToView
    )

    val consoleInteraction = ConsoleInteraction(System.`in`)
    val messageView = MessageViewImpl()
    val ui = ConsoleUIMessagesController(messegeQueueToView, run, messageView, consoleInteraction)

    val thread = Thread { executeWhileTrue(run, misplacedRecognizeController::runMisplacedTagRecognizerControllerTest) }
    val thread2 = Thread { executeWhileTrue(run, continuousReader::readNewRFIDTags) }
    thread2.start()
    thread.start()

    ui.runView()

    thread.join(10000)
    thread2.join(10000)

}

fun executeWhileTrue(boolean: AtomicBoolean, function: KFunction0<Unit>) {
    while (boolean.get()) {
        function.call()
    }
}
