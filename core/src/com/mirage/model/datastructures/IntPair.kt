package com.mirage.model.datastructures

data class IntPair (var x : Int, var y : Int){

    /**
     * Функции, которые возвращают точку, лежащую сверху/снизу/слева/справа от данной точки на расстоянии 1
     */
    fun top() : IntPair {
        return IntPair(x, y + 1)
    }

    fun bottom() : IntPair {
        return IntPair(x, y - 1)
    }

    fun left() : IntPair {
        return IntPair(x - 1, y)
    }

    fun right() : IntPair {
        return IntPair(x + 1, y)
    }

    /**
     * Функции, которые возвращают точку, лежащую сверху/снизу/слева/справа от данной точки на расстоянии 2
     */
    fun twiceTop() : IntPair {
        return IntPair(x, y + 2)
    }

    fun twiceBottom() : IntPair {
        return IntPair(x, y - 2)
    }

    fun twiceLeft() : IntPair {
        return IntPair(x - 2, y)
    }

    fun twiceRight() : IntPair {
        return IntPair(x + 2, y)
    }

    override fun toString(): String {
        return "[$x, $y]"
    }


}