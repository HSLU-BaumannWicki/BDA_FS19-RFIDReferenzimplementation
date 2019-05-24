package ch.bda.baumannwicki.data.supplier

import ch.bda.baumannwicki.data.LibraryCopy

interface LibraryCopySupplier {
    fun getLibraryCopyByID(identifier: String): LibraryCopy
}