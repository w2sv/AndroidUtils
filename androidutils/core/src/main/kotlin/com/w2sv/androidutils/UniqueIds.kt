@file:Suppress("unused")

package com.w2sv.androidutils

import java.util.PriorityQueue

open class UniqueIds(private val idBase: Int) : PriorityQueue<Int>() {

    fun addNewId(): Int =
        getNewId()
            .also {
                add(it)
            }

    fun addNewIds(count: Int): List<Int> =
        (0 until count)
            .map { addNewId() }

    private fun getNewId(): Int =
        lastOrNull()
            ?.let { it + 1 }
            ?: idBase
}