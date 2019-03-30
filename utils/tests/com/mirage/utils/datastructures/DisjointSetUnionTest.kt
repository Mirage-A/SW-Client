package com.mirage.utils.datastructures

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DisjointSetUnionTest {

    @Test
    fun testFloats() {
        val dsu = DisjointSetUnion<Float>()
        assertEquals(0f, dsu.findRoot(0f))
        dsu.makeSet(0f)
        dsu.makeSet(1f)
        dsu.makeSet(2f)
        assertEquals(0f, dsu.findRoot(0f))
        assertEquals(1f, dsu.findRoot(1f))
        assertEquals(2f, dsu.findRoot(2f))
        dsu.unite(-1f, 0f)
        dsu.unite(0f, 1f)
        assertEquals(0f, dsu.findRoot(0f))
        assertEquals(0f, dsu.findRoot(1f))
        dsu.unite(1f, 2f)
        assertEquals(0f, dsu.findRoot(2f))
    }

    @Test
    fun testPoints() {
        val dsu = DisjointSetUnion<MutablePoint>()
        assertEquals(MutablePoint(0f, 0f), dsu.findRoot(MutablePoint(0f, 0f)))
        dsu.makeSet(MutablePoint(0f, 0f))
        dsu.makeSet(MutablePoint(1f, 1f))
        dsu.makeSet(MutablePoint(2f, 2f))
        assertEquals(MutablePoint(0f, 0f), dsu.findRoot(MutablePoint(0f, 0f)))
        assertEquals(MutablePoint(1f, 1f), dsu.findRoot(MutablePoint(1f, 1f)))
        assertEquals(MutablePoint(2f, 2f), dsu.findRoot(MutablePoint(2f, 2f)))
        dsu.unite(MutablePoint(-1f, -1f), MutablePoint(0f, 0f))
        dsu.unite(MutablePoint(0f, 0f), MutablePoint(1f, 1f))
        assertEquals(MutablePoint(0f, 0f), dsu.findRoot(MutablePoint(0f, 0f)))
        assertEquals(MutablePoint(0f, 0f), dsu.findRoot(MutablePoint(1f, 1f)))
        dsu.unite(MutablePoint(1f, 1f), MutablePoint(2f, 2f))
        assertEquals(MutablePoint(0f, 0f), dsu.findRoot(MutablePoint(2f, 2f)))
    }
}