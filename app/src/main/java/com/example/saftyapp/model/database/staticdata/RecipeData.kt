package com.example.saftyapp.model.database.staticdata

import androidx.compose.ui.graphics.Color
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe

class RecipeData {
    companion object {
        val recipes = listOf(
            Recipe(
                name = "White Russian",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Pour vodka and coffee liqueur over ice cubes in an old-fashioned glass. Fill with light cream and serve.",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Coffee liqueur",
                        color = Color.Black,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Light cream",
                        color = Color.White,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vsrupw1472405732.jpg",
                color = null,
                allIngredients = listOf(
                    "Vodka: 2 oz",
                    " Coffee liqueur: 1 oz",
                    "Light cream"
                )
            ),
            Recipe(
                name = "Margarita",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Tequila",
                        color = Color.Transparent,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Triple sec",
                        color = Color.Yellow,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Lime juice",
                        color = Color.Green,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg",
                color = null,
                allIngredients = listOf(
                    "Tequila: 1 1/2 oz ",
                    "Triple sec: 1/2 oz ",
                    "Lime juice: 1 oz ",
                    "Salt"
                )
            ),
            Recipe(
                name = "Apple Berry Smoothie",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Throw everything into a blender and liquify.",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Berries",
                        color = Color.Red,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Apple",
                        color = Color.Red,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/xwqvur1468876473.jpg",
                color = null,
                allIngredients = listOf(
                    "Berries: 1 cup ",
                    "Apple: 2 "
                )
            ),
            Recipe(
                name = "Absolutely Fabulous",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Mix the Vodka and Cranberry juice together in a shaker and strain into a glass. Top up with Champagne.",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Cranberry Juice",
                        color = Color.Red,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Champagne",
                        color = Color.Yellow,
                        isUnlocked = true,
                    ),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/abcpwr1504817734.jpg",
                color = null,
                allIngredients = listOf(
                    "Vodka: 1 shot ",
                    "Cranberry Juice: 2 shots ",
                    "Champagne: Top up with"
                )
            ),
            Recipe(
                name = "Frappé",
                isCustom = false,
                isAlcoholic = false,
                instructions = "Mix together. Blend at highest blender speed for about 1 minute. Pour into a glass and drink with a straw. Notes: This works best if everything is cold (if you make fresh coffee, mix it with the milk and let it sit in the fridge for 1/2 hour. If it is not frothy, add more milk, or even just some more milk powder. The froth gradually turns to liquid at the bottom of the glass, so you will find that you can sit and drink this for about 1/2 hour, with more iced coffee continually appearing at the bottom. Very refreshing.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vqwryq1441245927.jpg",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Milk",
                        color = Color.White,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Coffee",
                        color = Color.Black,
                        isUnlocked = true,
                    ),
                ),
                color = null,
                allIngredients = listOf(
                    "Milk: 1/2 cup black ",
                    "Coffee: 1/2 cup ",
                    "Sugar: 1-2 tsp "
                )
            ),
            Recipe(
                name = "Bloody Mary",
                isCustom = false,
                isAlcoholic = true,
                instructions = "Stirring gently, pour all ingredients into highball glass. Garnish.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/t6caa21582485702.jpg",
                keyIngredients = listOf(
                    Ingredient(
                        name = "Vodka",
                        color = Color.Transparent,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Tomato juice",
                        color = Color.Red,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Lemon juice",
                        color = Color.Yellow,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Worcestershire sauce",
                        color = Color.Black,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Tabasco sauce",
                        color = Color.Red,
                        isUnlocked = true,
                    ),
                    Ingredient(
                        name = "Lime",
                        color = Color.Green,
                        isUnlocked = true,
                    )
                ),
                color = null,
                allIngredients = listOf(
                    "Vodka: 1 1/2 oz ",
                    "Tomato juice: 3 oz ",
                    "Lemon juice: 1 dash ",
                    "Worcestershire sauce: 1/2 tsp ",
                    "Tabasco sauce: 2-3 drops ",
                    "Lime: 1 wedge "
                )
            )
        )
    }
}