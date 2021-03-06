package rfid.communication

import ch.bda.baumannwicki.tagreader.devicecommunication.CommunicationDriver
import ch.bda.baumannwicki.tagreader.devicecommunication.DeviceCommunicationException
import ch.bda.baumannwicki.tagreader.devicecommunication.UnsupportedPlattformException
import com.sun.jna.*
import com.sun.jna.ptr.ByteByReference
import com.sun.jna.ptr.IntByReference
import rfid.communicationid.TagInformation

class HyientechDeviceCommunicationDriver(dllFile: String) : CommunicationDriver {

    private val hyientechDriver: HyientechDriver
    private val baudRatePointer = 0.toByte()
    private val frmHandlePointer = IntByReference(0)
    private val deviceAddressPointer = ByteByReference(Byte.MIN_VALUE)
    private val devicePortPointer = IntByReference()
    private var lastInventoryScanTime = 0.toByte()

    init {
        if (Platform.isWindows()) {
            hyientechDriver = Native.load(dllFile, HyientechDriver::class.java)
        } else {
            throw UnsupportedPlattformException("Hyientech Driver only supports a Windows 32Bit platform")
        }
    }


    fun initialize() {
        if (isError(
                hyientechDriver.AutoOpenComPort(
                    devicePortPointer,
                    deviceAddressPointer,
                    baudRatePointer,
                    frmHandlePointer
                )
            )
        ) {
            throw DeviceCommunicationException("Could not establish connection")
        }
        if (isError(hyientechDriver.OpenRf(deviceAddressPointer, frmHandlePointer.value))) {
            throw DeviceCommunicationException("Could not power on the inductive field")
        }
    }

    override fun readBlocks(from: Byte, numberOfBlocksToRead: Byte, tagInformation: TagInformation): List<Byte> {
        val uid = ByteByReference()
        uid.pointer = Memory(tagInformation.uid.size.toLong())
        val data = ByteByReference()
        data.pointer = Memory(50)
        val errorCode = ByteByReference()
        uid.pointer.write(0, tagInformation.uid.toByteArray(), 0, tagInformation.uid.size)
        val error = hyientechDriver.ReadMultipleBlock(
            deviceAddressPointer,
            AddressingIndicator.ADDRESSED_4BYTES_PER_BLOCK.byteByReference,
            uid,
            from,
            numberOfBlocksToRead,
            ByteByReference(),
            data,
            errorCode,
            frmHandlePointer.value
        )
        if (isError(error)) throw DeviceCommunicationException("Error occurred on reading Device. Error code provided by Hyientech was: $error ${errorCode.value}")
        return data.pointer.getByteArray(0, numberOfBlocksToRead * 4).toList()
    }

    private fun isError(errorCode: Int): Boolean {
        return errorCode > 0
    }

    override fun findAllRfids(): List<TagInformation> {
        var tags = ByteByReference()
        return createInventory(tags)
    }

    override fun findAllRfids(timeout: Int): List<TagInformation> {
        val timeoutByte = timeout.toByte()
        var inventoryScanTime = ByteByReference(timeoutByte)
        if(lastInventoryScanTime!=timeoutByte){
            hyientechDriver.WriteInventoryScanTime(this.deviceAddressPointer, inventoryScanTime, frmHandlePointer.value)
            lastInventoryScanTime = timeoutByte
        }
        return findAllRfids()
    }

    private fun createInventory(tags: ByteByReference, deleteInventory: Boolean = true): List<TagInformation> {
        var tagsInformation = ArrayList<TagInformation>()
        var nmrTags = ByteByReference()
        var tagsPointer: Pointer = Memory(255 * 9)
        tags.pointer = tagsPointer
        var value = hyientechDriver.Inventory(
            deviceAddressPointer,
            if (deleteInventory) CINTINUOS_WITHOUT_AFI else WITHOUT_AFI,
            GARBAGE_BYTE_REFERENCE,
            tags,
            nmrTags,
            frmHandlePointer.value
        )
        if (value == 0 || value == 14 || value == 11) {
            for (i in 1..nmrTags.value) {
                tagsInformation.add(TagInformation(tagsPointer.getByteArray(((i - 1) * 9 + 1).toLong(), 8).toList()))
            }
        }
        return tagsInformation

    }

    override fun isSingleTagReachable(tagInformation: TagInformation): Boolean {
        val uid = ByteByReference()
        uid.pointer = Memory(10.toLong())
        for (i in 0..tagInformation.uid.size - 1) {
            uid.pointer.setByte(i.toLong(), tagInformation.uid.reversed()[i])
        }
        val uidMemory = ByteByReference()
        uidMemory.pointer = Memory(2)
        uidMemory.pointer.setByte(0, 0)
        uidMemory.pointer.setByte(1, 0)
        return hyientechDriver.GetSystemInformation(
            deviceAddressPointer, ByteByReference(0), uid, ByteByReference(), uid, ByteByReference(0),
            ByteByReference(0), uidMemory, ByteByReference(0), ByteByReference(0), frmHandlePointer.value
        ) == 0
    }

    override fun switchToAntenna(antennaPosition: AntennaPosition) {
        val antennaStatus = ByteByReference((antennaPosition.getPositionAsInt()).toByte())
        hyientechDriver.SetActiveANT(deviceAddressPointer, antennaStatus, frmHandlePointer.value)
    }

    private interface HyientechDriver : Library {

        fun Select(
            ComAdr: ByteByReference, UID: ByteByReference,
            ErrorCode: ByteByReference, FrmHandle: Int
        ): Int

        fun GetSystemInformation(
            ComAdr: ByteByReference,
            State: ByteByReference,
            UIDI: ByteByReference,
            InformationFlag: ByteByReference,
            UIDO: ByteByReference,
            DSFID: ByteByReference,
            AFI: ByteByReference,
            MemorySize: ByteByReference,
            ICReference: ByteByReference,
            ErrorCode: ByteByReference,
            FrmHandle: Int
        ): Int

        fun OpenComPort(Port: Int, ComAdr: ByteByReference, Baud: ByteByReference, FrmHandle: IntByReference): Int
        fun CloseComPort(): Int
        fun CloseSpecComPort(FrmHandle: Int): Int
        fun OpenRf(comadr: ByteByReference, FrmHandle: Int): Int
        fun CloseRf(comadr: ByteByReference, FrmHandle: Int): Int
        fun WriteComAdr(ComAdr: ByteByReference, ComAdrData: ByteByReference, FrmHandle: Int): Int
        fun WriteInventoryScanTime(ComAdr: ByteByReference, InventoryScanTime: ByteByReference, FrmHandle: Int): Int
        fun SetGeneralOutput(ComAdr: ByteByReference, OutputData: ByteByReference, FrmHandle: Int): Int
        fun GetGeneralInput(ComAdr: ByteByReference, InputData: ByteByReference, FrmHandle: Int): Int
        fun SetRelay(ComAdr: ByteByReference, RelayAction: ByteByReference, FrmHandle: Int): Int
        fun SetActiveANT(ComAdr: ByteByReference, _ANT_Status: ByteByReference, FrmHandle: Int): Int
        fun GetANTStatus(ComAdr: ByteByReference, Get_ANT_Status: ByteByReference, FrmHandle: Int): Int
        fun SetUserDefinedBlockLength(ComAdr: ByteByReference, _Block_Len: ByteByReference, FrmHandle: Int): Int
        fun GetUserDefinedBlockLength(ComAdr: ByteByReference, _Block_Len: ByteByReference, FrmHandle: Int): Int
        fun SetScanMode(ComAdr: ByteByReference, _Scan_Mode_Data: ByteByReference, FrmHandle: Int): Int
        fun GetScanModeStatus(ComAdr: ByteByReference, _Scan_Mode_Status: ByteByReference, FrmHandle: Int): Int
        fun ReadScanModeData(ScanModeData: ByteByReference, ValidDataLength: IntByReference, FrmHandle: Int): Int
        fun SetAccessTime(ComAdr: ByteByReference, AccessTime: ByteByReference, FrmHandle: Int): Int
        fun GetAccessTime(ComAdr: ByteByReference, AccessTimeRet: ByteByReference, FrmHandle: Int): Int
        fun SetReceiveChannel(ComAdr: ByteByReference, ReceiveANT: ByteByReference, FrmHandle: Int): Int
        fun GetReceiveChannelStatus(ComAdr: ByteByReference, ReceiveANTStatus: ByteByReference, FrmHandle: Int): Int
        fun SetParseMode(ComAdr: ByteByReference, ParseMode: ByteByReference, FrmHandle: Int): Int
        fun GetParseMode(ComAdr: ByteByReference, ParseMode: ByteByReference, FrmHandle: Int): Int
        fun SetPwr(ComAdr: ByteByReference, _Pwr: ByteByReference, FrmHandle: Int): Int
        fun SetPwrByValue(ComAdr: ByteByReference, _PwrVal: ByteByReference, FrmHandle: Int): Int
        fun GetPwr(ComAdr: ByteByReference, _Pwr: ByteByReference, _PwrVal: ByteByReference, FrmHandle: Int): Int
        fun CheckAntenna(ComAdr: ByteByReference, _AntValid: ByteByReference, FrmHandle: Int): Int
        fun SyncScan(ComAdr: ByteByReference, _Sync: ByteByReference, FrmHandle: Int): Int
        fun GetReaderInformation(
            ComAdr: ByteByReference,
            VersionInfo: ByteByReference,
            ReaderType: ByteByReference,
            TrType: ByteByReference,
            InventoryScanTime: ByteByReference,
            FrmHandle: Int
        ): Int

        fun AutoOpenComPort(
            Port: IntByReference,
            ComAdr: ByteByReference,
            Baud: Byte,
            FrmHandle: IntByReference
        ): Int


        fun Inventory(
            ComAdr: ByteByReference,
            State: ByteByReference,
            AFI: ByteByReference,
            DSFIDAndUID: ByteByReference,
            CardNum: ByteByReference,
            FrmHandle: Int
        ): Int

        fun StayQuiet(ComAdr: ByteByReference, UID: ByteByReference, ErrorCode: ByteByReference, FrmHandle: Int): Int
        fun ReadSingleBlock(
            ComAdr: ByteByReference,
            State: ByteByReference,
            UID: ByteByReference,
            BlockNum: Byte,
            BlockSecStatus: ByteByReference,
            Data: ByteByReference,
            ErrorCode: ByteByReference,
            FrmHandle: Int
        ): Int

        fun WriteSingleBlock(
            ComAdr: ByteByReference,
            State: ByteByReference,
            UID: ByteByReference,
            BlockNum: Byte,
            Data: ByteByReference,
            ErrorCode: ByteByReference,
            FrmHandle: Int
        ): Int

        fun LockBlock(
            ComAdr: ByteByReference,
            State: ByteByReference,
            UID: ByteByReference,
            BlockNum: Byte,
            ErrorCode: ByteByReference,
            FrmHandle: Int
        ): Int

        fun ReadMultipleBlock(
            ComAdr: ByteByReference,
            State: ByteByReference,
            UID: ByteByReference,
            BlockNum: Byte,
            BlockCount: Byte,
            BlockSecStatus: ByteByReference,
            Data: ByteByReference,
            ErrorCode: ByteByReference,
            intFrmHandle: Int
        ): Int

    }

    enum class AddressingIndicator(val byteByReference: ByteByReference) {
        ADDRESSED_4BYTES_PER_BLOCK(ByteByReference(0)), SELECTED_4BYTES_PER_BLOCK(ByteByReference(1)),
        ADDRESSED_8BYTES_PER_BLOCK(ByteByReference(4)), SELECTED_8BYTES_PER_BLOCK(ByteByReference(5))
    }

    companion object {
        private val WITHOUT_AFI = ByteByReference(0)
        private val CINTINUOS_WITHOUT_AFI = ByteByReference(6)
        private val GARBAGE_BYTE_REFERENCE = ByteByReference(0)
    }
}
