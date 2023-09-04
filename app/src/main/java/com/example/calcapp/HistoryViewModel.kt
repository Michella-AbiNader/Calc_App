package com.example.calcapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val dao: HistoryDao
): ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.DeleteHistory -> {
                viewModelScope.launch {
                    dao.delete(event.history)
                }
            }

            HistoryEvent.HideHistory -> {
                _state.update {
                    it.copy(
                        isHistory = false
                    )
                }
            }

            HistoryEvent.SaveHistory -> {
                val equation = state.value.equation
                val answer = state.value.answer

                val history = History(equation, answer)
                viewModelScope.launch {
                    dao.insert(history)
                }

                _state.update {
                    it.copy(
                        equation = "",
                        answer = ""
                    )
                }
            }

            is HistoryEvent.SetAnswer -> {
                _state.update {
                    it.copy(
                        answer = event.answer
                    )
                }
            }

            is HistoryEvent.SetEquation -> {
                _state.update {
                    it.copy(
                        equation = event.equation
                    )
                }
            }

            HistoryEvent.ShowHistory -> {
                var history = emptyList<History>()
                viewModelScope.launch {
                    history = dao.display()
                }
                _state.update {
                    it.copy(
                        history = history,
                        isHistory = true
                    )
                }
            }

            HistoryEvent.ClearHistory -> {
                viewModelScope.launch {
                    dao.clearHistory()
                }
            }
        }
    }
}