package com.example.saftyapp.model.database.staticdata

import androidx.compose.ui.graphics.Color
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.Objects.Recipe

class RecipeData {
    companion object {
        val recipes = listOf(
            Recipe(
                name = "White Russian",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Pour vodka and coffee liqueur over ice cubes in an old-fashioned glass. Fill with light cream and serve.",
                ingredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                        measure = "2 oz"
                    ),
                    Ingredient(
                        name = "Coffee liqueur",
                        color = Color.Black,
                        isUnlocked = true,
                        measure = "1 oz"
                    ),
                    Ingredient(
                        name = "Light cream",
                        color = Color.White,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vsrupw1472405732.jpg",
                color = null
            ),
            Recipe(
                name = "Margarita",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.",
                ingredients = listOf(
                    Ingredient(
                        name = "Tequila",
                        color = Color.Transparent,
                        isUnlocked = true,
                        measure = "1 1/2 oz"
                    ),
                    Ingredient(
                        name = "Triple sec",
                        color = Color.Yellow,
                        isUnlocked = true,
                        measure = "1/2 oz"
                    ),
                    Ingredient(
                        name = "Lime juice",
                        color = Color.Green,
                        isUnlocked = true,
                        measure = "1 oz"
                    ),
                    Ingredient(
                        name = "Salt",
                        color = Color.White,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg",
                color = null
            ),
            Recipe(
                name = "Apple Berry Smoothie",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Throw everything into a blender and liquify.",
                ingredients = listOf(
                    Ingredient(
                        name = "Berries",
                        color = Color.Red,
                        isUnlocked = true,
                        measure = "1 cup"
                    ),
                    Ingredient(
                        name = "Apple",
                        color = Color.Red,
                        isUnlocked = true,
                        measure = "2"
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/xwqvur1468876473.jpg",
                color = null
            ),
            Recipe(
                name = "Absolutely Fabulous",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Mix the Vodka and Cranberry juice together in a shaker and strain into a glass. Top up with Champagne.",
                ingredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                        measure = "1 shot"
                    ),
                    Ingredient(
                        name = "Cranberry Juice",
                        color = Color.Red,
                        isUnlocked = true,
                        measure = "2 shots"
                    ),
                    Ingredient(
                        name = "Champagne",
                        color = Color.Yellow,
                        isUnlocked = true,
                        measure = "Top up with"
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/abcpwr1504817734.jpg",
                color = null
            ),
            Recipe(
                name = "Frappé",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Mix together. Blend at highest blender speed for about 1 minute. Pour into a glass and drink with a straw. Notes: This works best if everything is cold (if you make fresh coffee, mix it with the milk and let it sit in the fridge for 1/2 hour. If it is not frothy, add more milk, or even just some more milk powder. The froth gradually turns to liquid at the bottom of the glass, so you will find that you can sit and drink this for about 1/2 hour, with more iced coffee continually appearing at the bottom. Very refreshing.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vqwryq1441245927.jpg",
                ingredients = listOf(
                    Ingredient(
                        name = "Milk",
                        color = Color.White,
                        isUnlocked = true,
                        measure = "1/2 cup",
                    ),
                    Ingredient(
                        name = "Coffee",
                        color = Color.Black,
                        isUnlocked = true,
                        measure = "1/2 cup black"
                    ),
                    Ingredient(
                        name = "Sugar",
                        color = Color.White,
                        isUnlocked = true,
                        measure = "1-2 tsp"
                    )
                ),
                color = null
            ),
            Recipe(
                name = "Bloody Mary",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Stirring gently, pour all ingredients into highball glass. Garnish.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/t6caa21582485702.jpg",
                ingredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                        measure = "1 1/2 oz"
                    ),
                    Ingredient(
                        name = "Tomato juice",
                        color = Color.Red,
                        isUnlocked = true,
                        measure = "3 oz"
                    ),
                    Ingredient(
                        name = "Lemon juice",
                        color = Color.Yellow,
                        isUnlocked = true,
                        measure = "1 dash"
                    ),
                    Ingredient(
                        name = "Worcestershire sauce",
                        color = Color.Black,
                        isUnlocked = true,
                        measure = "1/2 tsp"
                    ),
                    Ingredient(
                        name = "Tabasco sauce",
                        color = Color.Red,
                        isUnlocked = true,
                        measure = "2-3 drops"
                    ),
                    Ingredient(
                        name = "Lime",
                        color = Color.Green,
                        isUnlocked = true,
                        measure = "1 wedge"
                    )
                ),
                color = null
            )
        )
    }
}