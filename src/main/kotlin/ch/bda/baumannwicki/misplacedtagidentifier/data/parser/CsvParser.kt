package ch.bda.baumannwicki.misplacedtagidentifier.data.parser

interface CsvParser<T> {
    fun parseDatasToString(data: List<T>): String
    fun parseStringToDatas(dataString: String): List<T>
    fun parseStringToData(dataString: String): T
}
