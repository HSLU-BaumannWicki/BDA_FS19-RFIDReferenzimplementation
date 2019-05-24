package ch.bda.baumannwicki.data

class LibraryCopy(
    val article: String,
    val box: String,
    val type: String,
    val layout: String,
    val subBox: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (super.equals(other)) return true
        if (other !is LibraryCopy) return false
        return article == other.article
    }

    override fun toString(): String {
        return "$article, $box, $type, $layout, $subBox"
    }
}
