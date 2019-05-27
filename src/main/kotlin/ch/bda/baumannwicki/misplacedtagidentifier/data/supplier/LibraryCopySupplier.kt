package ch.bda.baumannwicki.misplacedtagidentifier.data.supplier

import ch.bda.baumannwicki.misplacedtagidentifier.data.LibraryCopy

interface LibraryCopySupplier {
    fun getLibraryCopyByID(identifier: String): LibraryCopy
}
