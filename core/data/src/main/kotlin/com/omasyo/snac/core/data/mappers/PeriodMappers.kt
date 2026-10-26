package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.network.models.Period

fun TimePeriod.toPeriod() : Period = when(this) {
    TimePeriod.Daily -> Period.Daily
    TimePeriod.Weekly -> Period.Weekly
    TimePeriod.Monthly -> Period.Monthly
    TimePeriod.Yearly -> Period.Yearly
    TimePeriod.AllTime -> Period.All
}