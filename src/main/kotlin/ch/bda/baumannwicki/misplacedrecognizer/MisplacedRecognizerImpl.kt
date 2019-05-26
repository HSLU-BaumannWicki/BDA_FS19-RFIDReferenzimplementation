package ch.bda.baumannwicki.misplacedrecognizer

import ch.bda.baumannwicki.data.LibraryCopy

class MisplacedRecognizerImpl : MisplacedRecognizer {
    override fun getMostLikeleyBoxId(allTags: List<LibraryCopy>): List<LibraryCopy> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMisplacedTags(allTags: List<LibraryCopy>): List<LibraryCopy> {
        var map: MutableMap<String, Int> = HashMap<String, Int>()
        for (tag in allTags) {
            map[tag.box] = (map[tag.box] ?: 0) + 1
        }
        val resultList = map.toList().sortedBy { (_, value) -> value }.reversed().toMutableList()
        resultList.removeAt(0)
        val resultMap = resultList.toMap()
        return allTags.filter { resultMap.containsKey(it.box) }.toList()
    }
}