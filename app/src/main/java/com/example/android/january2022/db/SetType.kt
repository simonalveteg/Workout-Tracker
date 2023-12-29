package com.example.android.january2022.db

object SetType {
    const val WARMUP = "Warmup"
    const val EASY = "Easy"
    const val NORMAL = "Normal"
    const val HARD = "Hard"
    const val DROP = "Drop"

    private val order by lazy { listOf(WARMUP, EASY, NORMAL, HARD, DROP) }

    fun add(current: String): String {
        val currentIndex: Int = order.indexOf(current)
        val nextIndex = (currentIndex + 1) % order.size
        return order[nextIndex]
    }
    fun next(current: String): String = add(current)
}
