package com.mirage.model.datastructures

/**
 * Реализация системы непересекающихся множеств (СНМ)
 */
class DisjointSetUnion<T> {

    private val indexMap = HashMap<T, Int>()
    private val data = ArrayList<T>()
    private val par = ArrayList<Int>()
    private val rank = ArrayList<Int>()

    /**
     * Создаёт множество из одного элемента, если этот элемент ещё не содержится в СНМ
     */
    fun makeSet(element: T) {
        if (indexMap.containsKey(element)) return
        data.add(element)
        indexMap[element] = data.size - 1
        par.add(data.size - 1)
        rank.add(0)
    }

    /**
     * Возвращает представителя множества, содержащего данный элемент
     * Если элемента нет в СНМ, этот элемент возвращается
     */
    fun findRoot(element: T) : T {
        val index = indexMap[element]
        index ?: return element
        return data[findRoot(index)]
    }

    /**
     * Возвращает индекс представителя множества по индексу элемента
     */
    private fun findRoot(index: Int) : Int {
        if (index == par[index]) return index
        par[index] = findRoot(par[index])
        return par[index]
    }

    /**
     * Объединяет два множества, в которых лежат данные элементы, в одно
     */
    fun unite(first: T, second: T) {
        val firstIndex = indexMap[first]
        val secondIndex = indexMap[second]
        firstIndex ?: return
        secondIndex ?: return
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