package ch.bda.baumannwicki.data.parser

import ch.bda.baumannwicki.data.LibraryCopy

class CsvLibraryCopyParser : CsvParser<LibraryCopy> {
    override fun parseStringToData(dataString: String): LibraryCopy {
        if (dataString.contains("\n")) {
            throw IllegalArgumentException("dataString contains illegal \\n character -> dataString: $dataString")
        }
        return parseStringToDatas(dataString).get(0)
    }

    override fun parseStringToDatas(dataString: String): List<LibraryCopy> {
        var libraryCopys: MutableList<LibraryCopy> = mutableListOf()
        for (element: String in dataString.split("\n")) {
            var libraryPlainEntries: List<String> = element.split(",")
            if (libraryPlainEntries.size == 5) {
                val article: String = libraryPlainEntries[0]
                val box: String = libraryPlainEntries[1]
                val type: String = libraryPlainEntries[2]
                val layout: String = libraryPlainEntries[3]
                val subBox: String = libraryPlainEntries[4]
                libraryCopys.add(LibraryCopy(article, box, type, layout, subBox))
            } else {
                throw NotCompliantStringToParseCSVException()
            }
        }
        return libraryCopys
    }

    override fun parseDatasToString(data: List<LibraryCopy>): String {
        var returnString = ""
        for (libraryCopy: LibraryCopy in data) {
            returnString += "$libraryCopy\n"
        }
        return returnString.removeSuffix("\n").replace(" ", "")
    }

}
