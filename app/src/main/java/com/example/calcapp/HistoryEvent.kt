package com.example.calcapp

sealed interface HistoryEvent{
    object SaveHistory: HistoryEvent
    data class SetEquation(val equation: String):  HistoryEvent
    data class SetAnswer(val answer: String):  HistoryEvent
    object ShowHistory: HistoryEvent
    object HideHistory: HistoryEvent
    data class DeleteHistory(val history: History): HistoryEvent
    object ClearHistory: HistoryEvent

}