package com.mirage.model.scripts

import com.mirage.controller.Controller
import com.mirage.model.Model
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.luaj.vm2.LuaTable

object ScriptUtils {

    fun getModel() = Model

    fun getController() = Controller

    fun launchScriptAfterDelay(path: String, delayTime: Long, args: LuaTable) {
        GlobalScope.launch {
            delay(delayTime)
            runScript(path, args)
        }
    }

    fun launchScriptAfterDelay(path: String, delayTime: Long) {
        launchScriptAfterDelay(path, delayTime, LuaTable())
    }


    fun launchScriptAfterDelay(path: String) {
        launchScriptAfterDelay(path, 0L)
    }
}