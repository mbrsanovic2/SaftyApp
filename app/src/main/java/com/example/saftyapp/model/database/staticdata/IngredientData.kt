package com.example.saftyapp.model.database.staticdata

import androidx.compose.ui.graphics.Color
import com.example.saftyapp.model.database.entities.IngredientEntity

class IngredientData {
    companion object {
        val ingredients = listOf(
            IngredientEntity(
                name = "Apple",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/apple-small.png",
                color = Color(0x80EC9F00),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Berries",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/berries-small.png",
                color = Color(0xFF990033),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Chocolate",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/chocolate-small.png",
                color = Color(0xFF4D2600),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Coffee",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/coffee-small.png",
                color = Color(0xFF3B2F2F),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Cranberries",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cranberries-small.png",
                color = Color(0xFF9E1B32),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Grenadine",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Grenadine-small.png",
                color = Color(0xFFD1001C),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Kiwi",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Kiwi-small.png",
                color = Color(0xFF8EE53F),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Lime",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Lime-small.png",
                color = Color(0xFFBFFF00),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Mango",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Mango-small.png",
                color = Color(0xFFFFA62B),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Orange",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Orange-small.png",
                color = Color(0xFFFFA500),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Pineapple juice",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Pineapple%20juice-small.png",
                color = Color(0xFFFEEA5A),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Strawberries",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/strawberries-small.png",
                color = Color(0xFFFF0000),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Tea",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Tea-small.png",
                color = Color(0xFFD2A679),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Vodka",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Vodka-small.png",
                color = Color(0xFFEDEDED),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Yoghurt",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Yoghurt-small.png",
                color = Color(0xFFFFFDFC),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Banana",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/banana-small.png",
                color = Color(0xFFFFE135),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Milk",
                color = Color(0xFFFDFFFA),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/milk-small.png",
                isUnlocked = true
            ),


            // Ingredient not unlocked from beginning -> level up needed
            IngredientEntity(
                name = "Amaretto",
                color = Color(0xFF914E2D),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/amaretto-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Apple juice",
                color = Color(0x80EC9F00),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/apple%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Bourbon",
                color = Color(0xFF8B5C2E),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/bourbon-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Brandy",
                color = Color(0xFFA66E33),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/brandy-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Champagne",
                color = Color(0xFFF7E7B7),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/champagne-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Cider",
                color = Color(0xFFD9903D),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cider-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Cocoa powder",
                color = Color(0xFF4B2E1E),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cocoa%20powder-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Coffee liqueur",
                color = Color(0xFF3B2F2F),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/coffee%20liqueur-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Cognac",
                color = Color(0xFFA65F3E),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cognac-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Cranberry Juice",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cranberry%20juice-small.png",
                color = Color(0xFF9E1B32),
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Creme de Cassis",
                color = Color(0xFF4B0082),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/creme%20de%20cassis-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Dark rum",
                color = Color(0xFF3B1E08),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/dark%20rum-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Egg",
                color = Color(0xFFFFFDD0),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/egg-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Egg yolk",
                color = Color(0xFFFFC300),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/egg%20yolk-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Gin",
                color = Color(0xFFE0F8F7),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/gin-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Ginger",
                color = Color(0xFFD4A017),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/ginger-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Grapefruit juice",
                color = Color(0xFFFFB6C1),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/grapefruit%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Grapes",
                color = Color(0xFFE8E4B8),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/grapes-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Grape juice",
                color = Color(0xFF800080),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/grape%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Heavy cream",
                color = Color(0xFFFFFDD0),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/heavy%20cream-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Irish cream",
                color = Color(0xFF8B5D33),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/irish%20cream-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Jack Daniels",
                color = Color(0xFF3B1E08),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/jack%20daniels-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Kahlua",
                color = Color(0xFF362511),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/kahlua-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Lemon juice",
                color = Color(0xFFFFFF99),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/lemon%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Light cream",
                color = Color(0xFFFFFFFF),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/light%20cream-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Lime juice",
                color = Color(0xFFCCFF99),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/lime%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Lemon",
                color = Color(0xFFFFFF00),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/lemon-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Lemonade",
                color = Color(0xFFFFFFB2),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/lemonade-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Ouzo",
                color = Color(0xFFF5F5FF),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/ouzo-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Peach nectar",
                color = Color(0xFFFFDAB9),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/peach%20nectar-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Red wine",
                color = Color(0xFF8B0000),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/red%20wine-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Rum",
                color = Color(0xFF703C14),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/rum-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Scotch",
                color = Color(0xFFB8860B),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/scotch-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Sherry",
                color = Color(0xFF7B3F00),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/sherry-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Sprite",
                color = Color(0xFFEFFFFF),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/sprite-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Tabasco sauce",
                color = Color(0xFFFF4500),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/tabasco%20sauce-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Tequila",
                color = Color(0xFFFFE4B5),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/tequila-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Tomato juice",
                color = Color(0xFFFF6347),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/tomato%20juice-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Triple sec",
                color = Color(0xFFFFFAFA),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/triple%20sec-small.png",
                isUnlocked = false
            ),
            IngredientEntity(
                name = "Whiskey",
                color = Color(0xFFDEB887),
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/whiskey-small.png",
                isUnlocked = false
            ),
        )
    }
}