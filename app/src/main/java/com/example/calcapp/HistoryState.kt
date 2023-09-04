package com.example.calcapp

data class HistoryState(
    val history: List<History> = emptyList(),
    val equation: String = "",
    val answer: String = "",
    val isHistory: Boolean = false,


)