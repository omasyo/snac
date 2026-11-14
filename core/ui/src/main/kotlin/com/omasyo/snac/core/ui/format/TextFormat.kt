package com.omasyo.snac.core.ui.format

const val MetaSeparator = "·"

fun metaLine(vararg parts: String?): String = parts.filterNotNull().joinToString(" $MetaSeparator ")

fun formatEpisodeCode(season: Int, number: Int): String = "S%02dE%02d".format(season, number)
