package com.mirage.core


object Log {

    fun i(msg: Any?) {
        println(msg.toString())
    }

    fun d(msg: Any?) {
        println(msg.toString())
    }

    fun e(msg: Any?) {
        System.err.println(msg.toString())
    }
}
