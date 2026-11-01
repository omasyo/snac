package com.omasyo.snac.core.ui.format

fun metaLine(vararg parts: String?): String = parts.filterNotNull().joinToString(" · ")
