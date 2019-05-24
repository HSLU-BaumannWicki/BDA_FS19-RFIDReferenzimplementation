package ch.bda.baumannwicki.data.supplier

import ch.bda.baumannwicki.data.LibraryCopy
import ch.bda.baumannwicki.data.parser.CsvParser
import util.ReadableFile

class LibraryCopyCSVSupplier(val file: ReadableFile, val csvParser: CsvParser<LibraryCopy>) : LibraryCopySupplier {
    override fun getLibraryCopyByID(identifier: String): LibraryCopy {
        file.getReader().useLines { lines ->
            for (line in lines) {
                if (line.startsWith(identifier)) {
                    val potentialMatch = csvParser.parseStringToData(line)
                    if (potentialMatch.article == identifier) {
                        return potentialMatch
                    }
                }
            }
        }
        throw NoLibraryCopyFoundException()
    }
}