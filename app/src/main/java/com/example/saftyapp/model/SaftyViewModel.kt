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
    private val maxFill = 0.777f

    private val _currentExpression = MutableStateFlow(SaftyExpression.Happy)
    val currentExpression: StateFlow<SaftyExpression> = _currentExpression.asStateFlow()

    private val _fillTarget = MutableStateFlow(0f)
    val fillTarget = _fillTarget.asStateFlow()

    private val addedColors = mutableListOf<Color?>()
    private val _liquidColor = MutableStateFlow(Color.Transparent)
    val liquidColor = _liquidColor.asStateFlow()

    fun addIngredient(ingredientColor: Color?, wasRecommended: Boolean){
        mixCount++
        addedColors.add(ingredientColor)
        changeExpression(wasRecommended)
        updateFill()
    }

    fun removeIngredient(ingredientColor: Color?, wasRecommended: Boolean){
        mixCount--
        addedColors.remove(ingredientColor)
        updateFill()
    }

    fun drinkFinished(){
        _fillTarget.value = maxFill
    }

    fun cancelDrinkFinished(){
        updateFill()
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

    private fun updateFill() {
        val decay = 0.85f

        // maxFill - (maxFill * 0.777^mixCount) -> Rechter wert wird immer kleiner
        val newFill = maxFill - (maxFill * decay.pow(mixCount))
        _fillTarget.value = newFill.coerceAtMost(maxFill)

        updateColor()
    }

    private fun updateColor(){
        val colors = addedColors.filterNotNull()
        if(colors.isEmpty()) _liquidColor.value = Color.Transparent
        else {
            val r = colors.sumOf { it.red.toDouble() } / colors.size
            val g = colors.sumOf { it.green.toDouble() } / colors.size
            val b = colors.sumOf { it.blue.toDouble() } / colors.size

            _liquidColor.value = Color(r.toFloat(), g.toFloat(), b.toFloat())
        }
    }


}