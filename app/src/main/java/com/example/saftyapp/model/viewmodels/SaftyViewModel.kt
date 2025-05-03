package com.example.saftyapp.model.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.Objects.Recipe
import com.example.saftyapp.model.SaftyExpression
import com.example.saftyapp.model.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
import kotlin.random.Random

@HiltViewModel
class SaftyViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private var mixCount = 0
    private var happiness = 4
    private val maxFill = 0.777f

    private val _currentExpression = MutableStateFlow(SaftyExpression.Happy)
    val currentExpression: StateFlow<SaftyExpression> = _currentExpression.asStateFlow()

    private val _fillTarget = MutableStateFlow(0f)
    val fillTarget = _fillTarget.asStateFlow()

    private val _currentWords = MutableStateFlow("")
    val currentWords = _currentWords.asStateFlow()

    private val addedColors = mutableListOf<Color?>()
    private val _liquidColor = MutableStateFlow(Color.Transparent)
    val liquidColor = _liquidColor.asStateFlow()

    private val addedIngredients: MutableList<Ingredient> = mutableListOf()

    fun addIngredient(ingredient: Ingredient) {
        mixCount++
        addedIngredients.add(ingredient)
        addedColors.add(ingredient.color)
        updateFill()
        viewModelScope.launch {
            changeExpression(true)
            reactToIngredient(true)
        }
    }

    fun removeIngredient(ingredient: Ingredient) {
        mixCount--
        addedIngredients.remove(ingredient)
        addedColors.remove(ingredient.color)
        updateFill()
    }

    fun drinkFinished() {
        _fillTarget.value = maxFill
    }

    fun cancelDrinkFinished() {
        updateFill()
    }

    fun clearAllIngredients() {
        mixCount = 0
        addedColors.clear()
        updateFill()
        saftySpeaketh("")
    }

    suspend fun reactToIngredient(wasRecommended: Boolean) {
        val comments: List<String>
        if (wasRecommended) {
            comments = listOf(
                "Too \uD83C\uDF36 to \uFE0F\uD83D\uDC14",
                "Now that is rustikal\uD83D\uDE0F",
            )
        } else {
            comments = listOf(
                "You sleeping JESASS!",
                "Not very rustikale choice",
                "Very spaghetti carbonara",
            )
        }
        val randomComment = comments[Random.nextInt(comments.size)]
        // TODO: Random
        val recommendedIngredient = repository.RecipeFunctions().getIngredientRecommendations(addedIngredients)[0]
        saftySpeaketh(randomComment + ". Consider adding " + recommendedIngredient.name + " next ;)")
    }

    fun saftySpeaketh(fulltext: String) {
        _currentWords.value = fulltext
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

    private fun updateColor() {
        val colors = addedColors.filterNotNull()

        if (colors.isEmpty()) {
            _liquidColor.value = Color.Transparent
        } else {
            var totalAlpha = 0.0
            var weightedR = 0.0
            var weightedG = 0.0
            var weightedB = 0.0

            for (color in colors) {
                val alpha = color.alpha.toDouble()
                totalAlpha += alpha
                weightedR += color.red * alpha
                weightedG += color.green * alpha
                weightedB += color.blue * alpha
            }

            if (totalAlpha == 0.0) {
                _liquidColor.value = Color(180,230,255, 100)
            } else {
                val r = (weightedR / totalAlpha).toFloat()
                val g = (weightedG / totalAlpha).toFloat()
                val b = (weightedB / totalAlpha).toFloat()
                val a = (totalAlpha / colors.size).toFloat() // average alpha for final color

                _liquidColor.value = Color(r, g, b, a)
            }
        }
    }
}