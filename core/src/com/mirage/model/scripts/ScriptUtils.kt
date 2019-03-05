package com.mirage.model.scripts

import com.mirage.controller.Controller
import com.mirage.model.Model
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ScriptUtils {

    fun getModel() = Model

    fun getController() = Controller

    fun helloWorld(obj: Any?) {
        println(obj.toString() + " " + obj?.javaClass?.name + "hi!")
    }

    fun launchScriptAfterDelay(path: String, delayTime: Long, properties: Map<String, Any?>) {
        GlobalScope.launch {
            delay(delayTime)
            runScript(path, properties)
        }
    }

    fun launchScriptAfterDelay(path: String, delayTime: Long) {
        launchScriptAfterDelay(path, delayTime, HashMap())
    }


    fun launchScriptAfterDelay(path: String) {
        launchScriptAfterDelay(path, 0L)
    }
}