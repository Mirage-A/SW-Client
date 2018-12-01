package com.mirage.model.scene

class Scene {
    var c = Array(10, {it})

    init {
        for (a in c) {
            println(a)
        }
    }
}