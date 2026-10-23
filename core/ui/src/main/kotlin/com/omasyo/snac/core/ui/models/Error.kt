package com.omasyo.snac.core.ui.models

interface SnacError {
    val message: String
}

object GenericError : SnacError {
    override val message: String = "An error occurred"
}