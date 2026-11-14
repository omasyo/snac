package com.omasyo.snac.core.common.format

import java.text.DecimalFormat
import java.text.NumberFormat

fun Int.formatCount(): String = NumberFormat.getIntegerInstance().format(this)

fun Double.formatRating(maxFractionDigits: Int = 1): String =
    DecimalFormat("#.${"#".repeat(maxFractionDigits)}").format(this)

fun Long.formatRevenue(): String = when {
    this >= 1_000_000 -> "$%.1fM".format(this / 1_000_000.0)
    this >= 1_000 -> "$%.1fK".format(this / 1_000.0)
    else -> "$$this"
}
