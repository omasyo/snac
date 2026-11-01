package com.omasyo.snac.core.ui.format

const val MetaSeparator = "·"

fun metaLine(vararg parts: String?): String = parts.filterNotNull().joinToString(" $MetaSeparator ")
