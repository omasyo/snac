package com.omasyo.snac.feature.auth.impl

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

@Inject
class AuthActivityBinder internal constructor(
    private val authManager: AuthManager,
) {
    fun bind(activity: ComponentActivity) {
        handleRedirect(activity.intent)

        activity.addOnNewIntentListener(::handleRedirect)

        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) = authManager.onResumed()
        })

        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authManager.authorizationUrls.collect { url ->
                    CustomTabsIntent.Builder().build().launchUrl(activity, url.toUri())
                }
            }
        }
    }

    private fun handleRedirect(intent: Intent?) {
        val uri = intent?.data ?: return
        if (authManager.handleRedirect(uri)) intent.data = null
    }
}
