package com.mirage.model

class ModelFacade {
    private val logic = LogicThread()

    public fun startLogic() {
        logic.run()
    }
}