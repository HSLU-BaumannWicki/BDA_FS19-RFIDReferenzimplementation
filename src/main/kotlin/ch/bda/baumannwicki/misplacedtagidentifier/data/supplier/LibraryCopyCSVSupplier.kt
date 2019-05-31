package ch.bda.baumannwicki.misplacedtagidentifier.data.supplier

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy
import ch.bda.baumannwicki.misplacedtagidentifier.data.parser.CsvParser
import util.ReadableFile

class LibraryCopyCSVSupplier(val file: ReadableFile, val csvParser: CsvParser<LibraryCopy>) : LibraryCopySupplier {
    val fileList: List<String>
    init {
        fileList = file.getReader().readLines()
    }

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
        throw NoLibraryCopyFoundException("Could not find LibraryCopy for Identifier: $identifier")
    }
}
