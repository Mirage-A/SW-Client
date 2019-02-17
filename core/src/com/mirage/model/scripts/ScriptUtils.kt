package com.mirage.model.scripts

import com.badlogic.gdx.maps.MapProperties
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


object ScriptUtils {

    fun launchScriptAfterDelay(path: String, delayTime: Long, properties: MapProperties) {
        GlobalScope.launch {
            delay(delayTime)
            ScriptLoader.load(path).run(properties)
        }
    }

    fun launchScriptAfterDelay(path: String, delayTime: Long) {
        ScriptUtils.launchScriptAfterDelay(path, delayTime, MapProperties())
    }


    fun launchScriptAfterDelay(path: String) {
        ScriptUtils.launchScriptAfterDelay(path, 0L)
    }
}