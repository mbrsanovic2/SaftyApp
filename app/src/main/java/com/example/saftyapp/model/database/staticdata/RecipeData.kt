package com.example.saftyapp.model.database.staticdata

import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe

class RecipeData {
    companion object {
        val recipes = listOf(
            /** Eigene rezepte */
            Recipe(
                name = "Strawberry Banana Smoothie",
                instructions = "Add all ingredients into the blender and liquify. Add yoghurt until desired thickness is achieved.",
                keyIngredients = listOf(
                    Ingredient(name = "Strawberries"),
                    Ingredient(name = "Banana"),
                    Ingredient(name = "Yoghurt"),
                    Ingredient(name = "Milk")
                ),
                thumbnail = "https://www.gutekueche.at/storage/media/recipe/150153/resp/erdbeer-bananen-smoothie___webp_940_705.webp",
                allIngredients = listOf(
                    "Banana: 1",
                    "Strawberries: 250g ",
                    "Milk: 400ml ",
                    "Yoghurt: 200g ",
                    "Sugar: 0-2 tsp - until desired sweetness",
                    "Salt: small pinch"
                ),
            ),
            Recipe(
                name = "Mango Lassi",
                instructions = "Blend mango, yoghurt, and milk until smooth. Add sugar to taste. Serve chilled.",
                keyIngredients = listOf(
                    Ingredient(name = "Mango"),
                    Ingredient(name = "Yoghurt"),
                    Ingredient(name = "Milk")
                ),
                thumbnail = "https://www.gutekueche.at/storage/media/recipe/5550/resp/mango-lassi___webp_620_413.webp",
                allIngredients = listOf(
                    "Mango: 1 ripe, peeled and chopped",
                    "Yoghurt: 200g",
                    "Milk: 100ml",
                    "Sugar: 1-2 tsp (optional)",
                ),
            ),
            Recipe(
                name = "Hot Chocolate",
                instructions = "Heat milk in a saucepan but don't let it boil. Add chocolate in pieces and let it melt completely while stirring.",
                keyIngredients = listOf(
                    Ingredient(name = "Chocolate"),
                    Ingredient(name = "Milk")
                ),
                thumbnail = "https://www.gutekueche.at/storage/media/recipe/134148/resp/heisse-schokolade___webp_940_705.webp",
                allIngredients = listOf(
                    "Milk: 500ml",
                    "High Percentage chocolate: 100-150g",
                    "Cream: 100ml ",
                    "Sugar: to taste (optional)"
                ),
            ),
            Recipe(
                name = "Apple Berry Smoothie",
                instructions = "Throw everything into a blender and liquify.",
                keyIngredients = listOf(
                    Ingredient(name = "Berries"),
                    Ingredient(name = "Apple"),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/xwqvur1468876473.jpg",
                allIngredients = listOf(
                    "Berries: 1 cup ",
                    "Apple: 2 "
                )
            ),
            Recipe(
                name = "White Russian",
                instructions = "Pour vodka and coffee liqueur over ice cubes in an old-fashioned glass. Fill with light cream and serve.",
                keyIngredients = listOf(
                    Ingredient(name = "Vodka"),
                    Ingredient(name = "Coffee liqueur"),
                    Ingredient(name = "Light cream"),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vsrupw1472405732.jpg",
                allIngredients = listOf(
                    "Vodka: 2 oz",
                    " Coffee liqueur: 1 oz",
                    "Light cream"
                )
            ),
            Recipe(
                name = "Margarita",
                instructions = "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.",
                keyIngredients = listOf(
                    Ingredient(name = "Tequila"),
                    Ingredient(name = "Triple sec"),
                    Ingredient(name = "Lime juice"),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg",
                allIngredients = listOf(
                    "Tequila: 1 1/2 oz ",
                    "Triple sec: 1/2 oz ",
                    "Lime juice: 1 oz ",
                    "Salt"
                )
            ),
            Recipe(
                name = "Absolutely Fabulous",
                instructions = "Mix the Vodka and Cranberry juice together in a shaker and strain into a glass. Top up with Champagne.",
                keyIngredients = listOf(
                    Ingredient(name = "Vodka"),
                    Ingredient(name = "Cranberry Juice"),
                    Ingredient(name = "Champagne",),
                ),
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/abcpwr1504817734.jpg",
                allIngredients = listOf(
                    "Vodka: 1 shot ",
                    "Cranberry Juice: 2 shots ",
                    "Champagne: Top up with"
                )
            ),
            Recipe(
                name = "Frappé",
                instructions = "Mix together. Blend at highest blender speed for about 1 minute. Pour into a glass and drink with a straw. Notes: This works best if everything is cold (if you make fresh coffee, mix it with the milk and let it sit in the fridge for 1/2 hour. If it is not frothy, add more milk, or even just some more milk powder. The froth gradually turns to liquid at the bottom of the glass, so you will find that you can sit and drink this for about 1/2 hour, with more iced coffee continually appearing at the bottom. Very refreshing.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vqwryq1441245927.jpg",
                keyIngredients = listOf(
                    Ingredient(name = "Milk"),
                    Ingredient(name = "Coffee"),
                ),
                allIngredients = listOf(
                    "Milk: 1/2 cup black ",
                    "Coffee: 1/2 cup ",
                    "Sugar: 1-2 tsp "
                )
            ),
            Recipe(
                name = "Bloody Mary",
                instructions = "Stirring gently, pour all ingredients into highball glass. Garnish.",
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/t6caa21582485702.jpg",
                keyIngredients = listOf(
                    Ingredient(name = "Vodka"),
                    Ingredient(name = "Tomato juice"),
                    Ingredient(name = "Lemon juice"),
                    Ingredient(name = "Worcestershire sauce"),
                    Ingredient(name = "Tabasco sauce"),
                    Ingredient(name = "Lime")
                ),
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