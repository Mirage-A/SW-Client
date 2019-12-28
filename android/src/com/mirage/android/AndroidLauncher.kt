package com.mirage.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mirage.client.Client
import com.mirage.core.PLATFORM

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PLATFORM = "android"
        val cfg = AndroidApplicationConfiguration()
        cfg.useImmersiveMode = true
        initialize(Client, cfg)
    }
}
