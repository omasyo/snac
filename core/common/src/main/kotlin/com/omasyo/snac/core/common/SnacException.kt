package com.omasyo.snac.core.common

class SnacException(val error: SnacResult.Error) : Exception(error.toString())
