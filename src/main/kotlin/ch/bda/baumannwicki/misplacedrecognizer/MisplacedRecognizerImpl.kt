package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy

class MisplacedRecognizerImpl : MisplacedRecognizer {
    override fun getMostLikeleyBoxId(allTags: List<LibraryCopy>): List<LibraryCopy> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMisplacedTags(allTags: List<LibraryCopy>): List<LibraryCopy> {
        var map: MutableMap<String, Int> = HashMap()
        for (tag: LibraryCopy in allTags) {
            map[tag.box] = (map[tag.box] ?: 0) + 1
        }
        val resultList: MutableList<Pair<String, Int>> =
            map.toList().sortedBy { (_, value) -> value }.reversed().toMutableList()
        if (resultList.count() > 1 && resultList.first().second == resultList.last().second) {
            throw BoxIdentificationNotPossibleException("Equal number of Tags found for all identified Boxes")
        }
        resultList.removeAt(0)
        val resultMap: Map<String, Int> = resultList.toMap()
        return allTags.filter { resultMap.containsKey(it.box) }.toList()
    }
}