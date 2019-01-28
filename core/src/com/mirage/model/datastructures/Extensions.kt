package com.mirage.model.datastructures

import com.mirage.model.scene.ApproachabilityType

operator fun Array<IntArray>.get(indices: IntPair): Int {
    return this[indices.x][indices.y]
}

operator fun Array<BooleanArray>.get(indices: IntPair): Boolean {
    return this[indices.x][indices.y]
}

operator fun Array<Array<ApproachabilityType>>.get(indices: IntPair): ApproachabilityType {
    return this[indices.x][indices.y]
}

operator fun Array<IntArray>.set(indices: IntPair, value: Int) {
    this[indices.x][indices.y] = value
}

operator fun Array<BooleanArray>.set(indices: IntPair, value: Boolean) {
    this[indices.x][indices.y] = value
}

operator fun Array<Array<ApproachabilityType>>.set(indices: IntPair, value: ApproachabilityType) {
    this[indices.x][indices.y] = value
}
