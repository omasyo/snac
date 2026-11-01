package com.omasyo.snac.core.ui.format

import android.text.format.DateUtils
import java.time.LocalDateTime
import java.time.ZoneId

fun LocalDateTime.formatRelative(): String = DateUtils.getRelativeTimeSpanString(
    atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
    System.currentTimeMillis(),
    DateUtils.MINUTE_IN_MILLIS,
    DateUtils.FORMAT_ABBREV_RELATIVE,
).toString()
