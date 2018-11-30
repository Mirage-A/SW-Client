package com.mirage.model

public class ModelFacade {
    private val logic = LogicThread()

    public fun startLogic() {
        logic.run()
    }
}