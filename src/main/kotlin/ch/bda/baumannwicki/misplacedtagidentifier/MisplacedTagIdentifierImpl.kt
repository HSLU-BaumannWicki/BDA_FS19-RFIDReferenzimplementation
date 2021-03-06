package ch.bda.baumannwicki.misplacedtagidentifier

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy

class MisplacedTagIdentifierImpl : MisplacedTagIdentifier {
    override fun getBackRelationBoxId(allTags: List<LibraryCopy>): String {
        var map: MutableMap<String, Int> = getMapWithBoxAsKeyAndNumberOfCallsAsValue(allTags)
        val resultList: MutableList<Pair<String, Int>> = getListSortedByValueFromMap(map)
        throwErrorIfClearIdentificationNotPossible(resultList)
        return if(resultList.isNotEmpty()) resultList.removeAt(0).first else ""
    }

    override fun getMisplacedTags(allTags: List<LibraryCopy>): List<LibraryCopy> {
        var map: MutableMap<String, Int> = getMapWithBoxAsKeyAndNumberOfCallsAsValue(allTags)
        val resultList: MutableList<Pair<String, Int>> = getListSortedByValueFromMap(map)
        throwErrorIfClearIdentificationNotPossible(resultList)
        if (resultList.size <= 1){
            return emptyList()
        }
        resultList.removeAt(0)
        val resultMap: Map<String, Int> = resultList.toMap()
        return allTags.filter { resultMap.containsKey(it.box) }.toList()
    }

    private fun throwErrorIfClearIdentificationNotPossible(resultList: MutableList<Pair<String, Int>>) {
        if (resultList.count() > 1 && resultList.first().second == resultList.last().second) {
            throw BoxIdentificationNotPossibleException("Equal number of Tags found for all identified Boxes")
        }
    }

    private fun getListSortedByValueFromMap(map: MutableMap<String, Int>): MutableList<Pair<String, Int>> {
        val resultList: MutableList<Pair<String, Int>> =
            map.toList().sortedBy { (_, value) -> value }.reversed().toMutableList()
        return resultList
    }

    private fun getMapWithBoxAsKeyAndNumberOfCallsAsValue(allTags: List<LibraryCopy>): MutableMap<String, Int> {
        var map: MutableMap<String, Int> = HashMap()
        for (tag: LibraryCopy in allTags) {
            map[tag.box] = (map[tag.box] ?: 0) + 1
        }
        return map
    }
}
