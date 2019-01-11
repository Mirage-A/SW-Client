package model



/**
 * Кадр анимации
 */
class Frame() {
    /**
     * Список слоёв на кадре
     */
    var layers : ArrayList<Layer> = ArrayList()
    /**
     * Текущий выбранный слой
     */
    var curLayer : Int = -1

    constructor(origin : Frame) : this(){
        curLayer = origin.curLayer
        for (originLayer in origin.layers) {
            layers.add(Layer(originLayer))
        }
    }
}