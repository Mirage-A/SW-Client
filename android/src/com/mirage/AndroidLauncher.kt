package com.mirage

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.mirage.controller.Controller
import com.mirage.controller.Platform

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Platform.ASSETS_PATH = ""
        Platform.TYPE = Platform.Types.ANDROID
        val config = AndroidApplicationConfiguration()
        initialize(Controller, config)
    }
}
