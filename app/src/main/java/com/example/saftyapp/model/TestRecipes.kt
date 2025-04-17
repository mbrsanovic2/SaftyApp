package com.example.saftyapp.model

class TestRecipes {
    val recipes: Array<Recipe> = arrayOf(
        Recipe(
            name = "Apple Berry Smoothie",
            id = 12710,
            isCustom = false,
            alcoholic = false,
            instructions = "Throw everything into a blender and liquify.",
            ingredients = arrayOf(
                "Berries",
                "Apple"
            ),
            measures = arrayOf("1 cup", "2"),
            thumbnail = "https://www.thecocktaildb.com/images/media/drink/xwqvur1468876473.jpg"
        ),
        Recipe(
            name = "Kiwi Papaya Smoothie",
            id = 12714,
            isCustom = false,
            alcoholic = false,
            instructions = "Throw everything into a blender and liquify.",
            ingredients = arrayOf(
                "Kiwi",
                "Papaya"
            ),
            measures = arrayOf("3", "1/2"),
            thumbnail = "https://www.thecocktaildb.com/images/media/drink/jogv4w1487603571.jpg"
        ),
        Recipe(
            name = "Iced Coffee",
            id = 12770,
            alcoholic = false,
            isCustom = false,
            instructions = "Mix together until coffee and sugar is dissolved. Add milk. Shake well. Using a blender or milk shake maker produces a very foamy drink. Serve in coffee mug.",
            ingredients = arrayOf(
                "Coffee",
                "Sugar",
                "Water",
                "Milk"
            ),
            measures = arrayOf(
                "1/4 cup instant",
                "1/4 cup",
                "1/4 cup hot",
                "4 cups cold"
            ),
            thumbnail = "https://www.thecocktaildb.com/images/media/drink/ytprxy1454513855.jpg"
        )
    )
}