package ch.bda.bauparseStringToDatamannwicki.data.supplier

import ch.bda.baumannwicki.data.LibraryCopy
import ch.bda.baumannwicki.data.parser.CsvLibraryCopyParser
import ch.bda.baumannwicki.data.supplier.LibraryCopyCSVSupplier
import org.junit.jupiter.api.Test
import util.ReadableFileStub
import kotlin.test.assertEquals

class LibraryCopyCSVSupplierTest {

    @Test
    fun getLibraryCopyByID() {
        val file = ReadableFileStub("A,S,D,F,\nwet")
        val testee = LibraryCopyCSVSupplier(file, CsvLibraryCopyParser())
        val result = testee.getLibraryCopyByID("A")
        assertEquals(LibraryCopy("A", "S", "D", "F"), result)
    }

    @Test
    fun getLibraryCopyByID2() {
        val file = ReadableFileStub("A,S,D,F,\nD,E,F,C,")
        val testee = LibraryCopyCSVSupplier(file, CsvLibraryCopyParser())
        val result = testee.getLibraryCopyByID("D")
        assertEquals(LibraryCopy("D", "E", "F", "C"), result)
    }

    @Test
    fun getLibraryCopyByID3() {
        val file = ReadableFileStub("A,S,D,F,\nASDFIO,ASD,ASDFwerq2324,ijasdfo,\nD,E,F,C,")
        val testee = LibraryCopyCSVSupplier(file, CsvLibraryCopyParser())
        val result = testee.getLibraryCopyByID("ASDFIO")
        assertEquals(LibraryCopy("ASDFIO", "ASD", "ASDFwerq2324", "ijasdfo"), result)
    }
}
