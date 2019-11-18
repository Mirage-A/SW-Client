package com.mirage.utils.messaging

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class EventSubjectAdapterTest{

    @Test
    fun testAdapter() {
        val subj = EventSubjectAdapter<Int>()
        val expected = listOf(1, 2, 3, 4, 5, 6)
        subj.onNext(1)
        val actual = Collections.synchronizedList<Int>(ArrayList<Int>())
        val subs = subj.subscribe {actual.add(it)}
        subj.onNext(2)
        subj.onNext(3)
        subj.onNext(4)
        subs.unsubscribe()
        subj.onNext(5)
        subj.subscribe {actual.add(it)}
        subj.onNext(6)
        assertEquals(expected, actual)
    }
}