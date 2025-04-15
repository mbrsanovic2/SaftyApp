package com.example.saftyapp.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class SaftyViewModel : ViewModel() {
    private var mixCount = 0
    private var happiness = 4

    private val _currentExpression = MutableStateFlow(SaftyExpression.Happy)
    val currentExpression: StateFlow<SaftyExpression> = _currentExpression.asStateFlow()

    private val _fillTarget = MutableStateFlow(0f)
    val fillTarget = _fillTarget.asStateFlow()

    private val _liquidColor = MutableStateFlow(Color(255,255,255))
    val liquidColor = _liquidColor.asStateFlow()

    fun addIngredient(ingredientColor: Color?, wasRecommended: Boolean){
        increaseFill(ingredientColor)
        changeExpression(wasRecommended)
    }

    private fun changeExpression(wasRecommended: Boolean) {
        if (!wasRecommended) happiness--
        else happiness++

        happiness = happiness.coerceIn(1, 5)

        _currentExpression.value = when (happiness) {
            in 4..5 -> SaftyExpression.Happy
            3 -> SaftyExpression.Unimpressed
            2 -> SaftyExpression.Mad
            else -> SaftyExpression.Angry
        }
    }

    private fun increaseFill(color: Color?) {
        val start = 0.2f
        val decay = 0.85f

        val newFill = if (mixCount == 0) {
            start
        } else {
            1f - (1f - start) * decay.pow(mixCount)
        }

        _fillTarget.value = newFill.coerceAtMost(1f)

        if (color != null) {
            val current = _liquidColor.value
            val blendStrength = 1f / (mixCount + 1)
            val mixed = lerp(current, color, blendStrength)
            _liquidColor.value = mixed
        }

        mixCount++
    }
}