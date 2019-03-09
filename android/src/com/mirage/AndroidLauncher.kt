package com.mirage

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mirage.controller.Controller
import com.mirage.gamelogic.config

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config.put("assets", "")
        config.put("platform", "android")
        val cfg = AndroidApplicationConfiguration()
        cfg.useImmersiveMode = true
        initialize(Controller, cfg)
    }
}
