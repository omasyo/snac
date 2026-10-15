package com.omasyo.snac.feature.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.text.BasicText
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class AuthActivity : ComponentActivity() {

    var shouldExit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setFinishOnTouchOutside(false)

        val intent = CustomTabsIntent.Builder()
            .build().intent.apply {
                data = generateUrl(RedirectUri).toUri()
            }

        val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                lifecycleScope.launch {
                    delay(1_000.milliseconds)
                    if (shouldExit) {
                        Toast.makeText(this@AuthActivity, "User cancelled", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    }
                }
            }
        launcher.launch(intent)


        setContent {
            SnacTheme {
                BasicText("Loading")
            }

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shouldExit = false

        val callbackURI = intent.data

        val code = callbackURI?.getQueryParameter("code") ?: finish()

        Log.i("TAG", "onNewIntent: Got code $code")
    }
}
