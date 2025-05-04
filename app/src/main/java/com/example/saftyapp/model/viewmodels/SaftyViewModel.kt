package com.example.saftyapp.model.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.Objects.Ingredient
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
    private var happiness = 50
    private val maxFill = 0.777f
    private val addedIngredients: MutableList<Ingredient> = mutableListOf()
    private var acceptedIngredients: List<Ingredient>? = null
    private var recommendedIngredient: Ingredient? = null

    private val _currentExpression = MutableStateFlow(SaftyExpression.Happy)
    val currentExpression: StateFlow<SaftyExpression> = _currentExpression.asStateFlow()

    private val _fillTarget = MutableStateFlow(0f)
    val fillTarget = _fillTarget.asStateFlow()

    private val _currentWords = MutableStateFlow("")
    val currentWords = _currentWords.asStateFlow()

    private val addedColors = mutableListOf<Color?>()
    private val _liquidColor = MutableStateFlow(Color.Transparent)
    val liquidColor = _liquidColor.asStateFlow()

    private val _saftyGone = MutableStateFlow(false)
    val saftyGone = _saftyGone.asStateFlow()

    fun addIngredient(ingredient: Ingredient): Boolean {
        if(_saftyGone.value) return false

        when {
            // Case 1: First Ingredient or Ingredient recommended
            acceptedIngredients == null || ingredient == recommendedIngredient -> {
                acceptIngredient(ingredient, 25)
            }
            // Case 2: Not recommended but accepted
            acceptedIngredients?.contains(ingredient) == true -> {
                acceptIngredient(ingredient, -15)
            }
            // Case 3: Completely ignored Safty
            else -> {
                viewModelScope.launch {
                    reactToIngredient(-30)
                    changeHappinessLevel(-30)
                }
                return false
            }
        }
        return true
    }

    private fun acceptIngredient(ingredient: Ingredient, recommendationScore: Int){
        mixCount++
        addedIngredients.add(ingredient)
        addedColors.add(ingredient.color)
        updateFill()
        viewModelScope.launch {
            reactToIngredient(recommendationScore)
            changeHappinessLevel(recommendationScore)
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
        happiness = 75
        _saftyGone.value = false
        addedColors.clear()
        addedIngredients.clear()
        acceptedIngredients = null
        recommendedIngredient = null

        updateFill()
        updateExpression()
        saftySpeaketh("")
    }

    fun saftySpeaketh(fulltext: String) {
        _currentWords.value = fulltext
    }

    private suspend fun reactToIngredient(recommendationScore: Int) {
        var comments: List<String> = listOf("Unexpected State!")

        when (happiness) {
            in 75..100 -> {
                if(recommendationScore > 0) {
                    comments = listOf(
                        "Des ist ein \uD83C\uDF36 \uFE0F\uD83D\uDC14",
                        "Its getting rustikaler \uD83D\uDE0F",
                        "It gets better \uD83D\uDE0F"
                    )
                }else if(recommendationScore >= -15){
                    comments = listOf(
                        "Also a good choice",
                        "Very fitting as well",
                    )
                }else{
                    comments = listOf(
                        "Lets not add that",
                        "Lets leave that out",
                    )
                }
            }
            in 50..75 -> {
                if(recommendationScore > 0) {
                    comments = listOf(
                        "Des ist ein \uD83C\uDF36 \uFE0F\uD83D\uDC14",
                        "Its getting rustikaler \uD83D\uDE0F",
                        "It gets better \uD83D\uDE0F"
                    )
                }else if(recommendationScore >= -15){
                    comments = listOf(
                        "A okay choice",
                        "Sure you can add this",
                    )
                }else{
                    comments = listOf(
                        "A very bad idea",
                        "This does not fit well",
                    )
                }
            }
            in 1..50 -> {
                if(recommendationScore > 0) {
                    comments = listOf(
                        "Finally a good choice",
                        "Lets continue like this",
                    )
                }else if(recommendationScore >= -15){
                    comments = listOf(
                        "Why am I here",
                        "Don't ignore me",
                    )
                }else{
                    comments = listOf(
                        "You sleeping JESASS!",
                        "Not very rustikale choice",
                        "Very spaghetti carbonara",
                    )
                }
            }
            0 -> {
                _saftyGone.value = true
                saftySpeaketh("Au revoir frerot!")
                return
            }
        }

        val randomComment = comments[Random.nextInt(comments.size)]

        acceptedIngredients = repository.RecipeFunctions().getIngredientRecommendations(addedIngredients)
        recommendedIngredient = acceptedIngredients?.randomOrNull()

        val newHappyness = happiness + recommendationScore

        val recommendationComment = when (newHappyness) {
            in 76..100 -> ". Consider adding "
            in 50..75 -> ". Please add "
            in 25..50 -> ". I hope you add "
            else -> ". You have to add "
        }

        val message = recommendedIngredient?.let {
            "$randomComment$recommendationComment${it.name} next"
        } ?: "$randomComment. No more ideas for now ;)"

        saftySpeaketh(message)
    }

    private fun changeHappinessLevel(recommendationScore: Int) {
        happiness += recommendationScore

        happiness = happiness.coerceIn(0, 100)
        updateExpression()
    }


    private fun updateFill() {
        val decay = 0.85f

        // maxFill - (maxFill * 0.777^mixCount) -> Rechter wert wird immer kleiner
        val newFill = maxFill - (maxFill * decay.pow(mixCount))
        _fillTarget.value = newFill.coerceAtMost(maxFill)

        updateColor()
    }

    private fun updateExpression() {
        _currentExpression.value = when (happiness) {
            in 75..100 -> SaftyExpression.Happy
            in 50..75 -> SaftyExpression.Unimpressed
            in 25..50 -> SaftyExpression.Mad
            else -> SaftyExpression.Angry
        }
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