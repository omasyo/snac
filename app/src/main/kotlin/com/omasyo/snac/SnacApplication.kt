package com.omasyo.snac

import android.app.Application
import com.omasyo.snac.di.AppGraph
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class SnacApplication : Application(), MetroApplication {
    override val appComponentProviders: MetroAppComponentProviders by lazy { createGraphFactory<AppGraph.Factory>().create(this) }
}
