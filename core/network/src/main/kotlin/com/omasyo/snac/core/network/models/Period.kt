package com.omasyo.snac.core.network.models

enum class Period(val slug: String) {
    Daily("daily"),
    Weekly("weekly"),
    Monthly("monthly"),
    Yearly("yearly"),
    AllTime("all"),
}