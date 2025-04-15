package com.example.saftyapp.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SaftyViewModel : ViewModel() {

    private val _currentExpression = MutableStateFlow(SaftyExpression.Happy)
    val currentExpression: StateFlow<SaftyExpression> = _currentExpression.asStateFlow()

    private val _fillTarget = MutableStateFlow(0f)
    val fillTarget = _fillTarget.asStateFlow()

    fun changeExpression() {
        _currentExpression.value = SaftyExpression.entries
            .filterNot { it == _currentExpression.value }
            .random()
    }

    fun increaseFill() {
        _fillTarget.value = (_fillTarget.value + 0.1f).coerceAtMost(1f)
    }
}