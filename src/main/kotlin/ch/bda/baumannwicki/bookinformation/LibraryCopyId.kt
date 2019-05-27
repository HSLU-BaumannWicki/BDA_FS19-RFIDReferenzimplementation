package ch.bda.baumannwicki.bookinformation

class LibraryCopyId(private val uid: List<Byte>) {
    override fun toString(): String = uid.filter { it in 48..122 }.map(Byte::toChar).joinToString("")
}