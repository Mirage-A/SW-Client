package com.mirage.utils.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.luaj.vm2.lib.jse.CoerceJavaToLua

internal class UtilityExtensionsKtTest {

    @Test
    fun testLuaTable() {
        val table = tableOf(Pair("String", 1234), Pair("Int", 43L))
        assertEquals(CoerceJavaToLua.coerce(1234), table["String"])
        assertEquals(CoerceJavaToLua.coerce(43L), table["Int"])
    }

    @Test
    fun testUtils() {
        assertEquals(1, mapOf(2 to 1)[2])
        val set = treeSetOf(1, 3, 5)
        assertEquals(3, set.second())
        set += 2
        assertEquals(2, set.second())
        set += 0
        assertEquals(1, set.second())
    }
}