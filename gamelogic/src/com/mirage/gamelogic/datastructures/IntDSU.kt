package com.mirage.gamelogic.datastructures

/**
 * СНМ фиксированного размера с элементами от 0 до size - 1
 */
class IntDSU(size: Int) {

    private val par : IntArray
    private val rank : IntArray

    init {
        par = IntArray(size) {i -> i}
        rank = IntArray(size) {0}
    }

    /**
     * Возвращает индекс представителя множества по индексу элемента
     * Если индекс выходит за рамки размера СНМ, выбрасывается исключение
     */
    fun findRoot(index: Int) : Int {
        if (index == par[index]) return index
        par[index] = findRoot(par[index])
        return par[index]
    }

    /**
     * Объединяет два множества, в которых лежат данные элементы, в одно
     * Если индексы выходят за рамки размера СНМ, выбрасывается исключение
     */
    fun unite(firstIndex: Int, secondIndex: Int) {
        val firstRoot = findRoot(firstIndex)
        val secondRoot = findRoot(secondIndex)
        if (firstRoot == secondRoot) return
        when (true) {
            rank[firstRoot] == rank[secondRoot] -> {
                ++rank[firstRoot]
                par[secondRoot] = firstRoot
            }
            rank[firstRoot] > rank[secondRoot] -> {
                par[secondRoot] = firstRoot
            }
            else -> {
                par[firstRoot] = secondRoot
            }
        }
    }

}