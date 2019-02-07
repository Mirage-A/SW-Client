package com.mirage

import com.badlogic.gdx.backends.iosrobovm.IOSApplication
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration
import com.mirage.controller.Controller
import com.mirage.controller.Platform
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.UIApplication

class IOSLauncher : IOSApplication.Delegate() {
    override fun createApplication(): IOSApplication {
        // TODO выбрать корректный путь папки assets
        Platform.ASSETS_PATH = "./android/assets/"
        Platform.TYPE = Platform.Types.IOS
        val config = IOSApplicationConfiguration()
        return IOSApplication(Controller, config)
    }

    companion object {

        @JvmStatic
        fun main(argv: Array<String>) {
            val pool = NSAutoreleasePool()
            UIApplication.main<UIApplication, IOSLauncher>(argv, null, IOSLauncher::class.java)
            pool.close()
        }
    }
}