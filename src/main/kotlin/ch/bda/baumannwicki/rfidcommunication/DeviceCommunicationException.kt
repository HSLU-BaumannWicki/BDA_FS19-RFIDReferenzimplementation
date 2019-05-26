package ch.bda.baumannwicki.rfidcommunication

class DeviceCommunicationException(message: String) :
    Throwable("Error during the Communication with the Hyientech device: %s".format(message)) {
    constructor() : this("")
}
