package com.omasyo.snac

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.feature.auth.impl.AuthActivityBinder
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.metroViewModel

@ContributesIntoMap(AppScope::class, binding<Activity>())
@ActivityKey
@Inject
class MainActivity(
    private val metroVmf: MetroViewModelFactory,
    private val authActivityBinder: AuthActivityBinder,
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authActivityBinder.bind(this)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalMetroViewModelFactory provides metroVmf) {
                val viewModel: MainViewModel = metroViewModel()
                SnacTheme {
                    AppNavHost()
                    Text(viewModel.isAuthenticated.collectAsState().value.toString())
                }
            }
        }
    }
}
