package com.omasyo.snac.core.ui.format

import android.text.format.DateFormat
import android.text.format.DateUtils
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun LocalDate.formatDayMonth(): String = formatBestPattern("dMMM")

fun LocalDate.formatFullDate(): String = formatBestPattern("yMMMd")

private fun LocalDate.formatBestPattern(skeleton: String): String {
    val locale = Locale.getDefault()
    val pattern = DateFormat.getBestDateTimePattern(locale, skeleton)
    return format(DateTimeFormatter.ofPattern(pattern, locale))
}

fun LocalDateTime.formatRelative(): String = DateUtils.getRelativeTimeSpanString(
    atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE,
).toString()

fun Instant.formatLocalTime(): String = atZone(ZoneId.systemDefault())
    .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
