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
                color = Color(0xFF261A0D),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Cranberries",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cranberries-small.png",
                color = Color(0xFFCC0000),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Grenadine",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Grenadine-small.png",
                color = Color(0xFF800000),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Kiwi",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Kiwi-small.png",
                color = Color(0xFF00CC00),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Lime",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Lime-small.png",
                color = Color(0xFFCCFF66),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Mango",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Mango-small.png",
                color = Color(0xFFFFD633),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Orange",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Orange-small.png",
                color = Color(0xFFFF751A),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Pineapple juice",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Pineapple%20juice-small.png",
                color = Color(0xFFFFFF00),
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
                color = Color(0x00000000),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Yoghurt",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/Yoghurt-small.png",
                color = Color(0xFFFFFFFF),
                isUnlocked = true
            ),

            IngredientEntity(
                name = "Banana",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/banana-small.png",
                color = Color(0xFFECE297),
                isUnlocked = true
            ),
            IngredientEntity(
                name = "Milk",
                color = Color.White,
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/milk-small.png",
                isUnlocked = true
            ),


            // Ingredient not unlocked from beginning -> level up needed
            IngredientEntity(name = "Amaretto", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Apple juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Baileys irish cream", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Bourbon", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Brandy", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Champagne", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Cider", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Cocoa powder", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Coffee liqueur", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Cognac", color = Color(1), isUnlocked = false),
            IngredientEntity(
                name = "Cranberry Juice",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/cranberry%20juice-small.png",
                color = Color(1),
                isUnlocked = false
            ),
            IngredientEntity(name = "Creme de Cassis", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Dark rum", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Egg", color = Color.Yellow, isUnlocked = false),
            IngredientEntity(name = "Egg yolk", color = Color.Yellow, isUnlocked = false),
            IngredientEntity(
                name = "Gin",
                iconFilePath = "https://www.thecocktaildb.com/images/ingredients/gin-small.png",
                color = Color(1),
                isUnlocked = false
            ),
            IngredientEntity(name = "Ginger", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Grapefruit juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Grapes", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Grape juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Heavy cream", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Irish cream", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Jack Daniels", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Kahlua", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Lemon juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Light cream", color = Color.White, isUnlocked = false),
            IngredientEntity(name = "Lime juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Lemon", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Lemonade", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Ouzo", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Peach nectar", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Red wine", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Rum", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Scotch", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Sherry", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Sprite", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Tabasco sauce", color = Color.Red, isUnlocked = false),
            IngredientEntity(name = "Tequila", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Tomato juice", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Triple sec", color = Color(1), isUnlocked = false),
            IngredientEntity(name = "Whiskey", color = Color(1), isUnlocked = false),
            IngredientEntity(
                name = "Worcestershire sauce",
                color = Color.Black,
                isUnlocked = false
            ),


            // Ingredients to be excluded
            //IngredientEntity(name = "7-Up", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Absolut Citron", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Ale", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Angelica root", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Apple brandy", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Apple cider", color = Color.Yellow, isUnlocked = true),
            //IngredientEntity(name = "Applejack", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Apricot brandy", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Añejo rum", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Bitters", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Blackberry brandy", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Blended whiskey", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Cantaloupe", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Carbonated water", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Cherry brandy", color = Color(1), isUnlocked = true),
            // IngredientEntity(name = "Chocolate liqueur", color = Color(1), isUnlocked = false),
            // IngredientEntity(name = "Chocolate syrup", color = Color(1), isUnlocked = false),
            //IngredientEntity(name = "Coffee brandy", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Creme de Cacao", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Dry Vermouth", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Dubonnet Rouge", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Espresso", iconFilePath = "https://www.thecocktaildb.com/images/ingredients/espresso-small.png", color = Color.Black,isUnlocked = true),
            //IngredientEntity(name = "Everclear", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Firewater", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Galliano", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Irish whiskey", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Johnnie Walker", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Lager", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Lemon vodka", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Light rum", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Midori melon liqueur", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Orange bitters", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Peach Vodka", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Peppermint schnapps", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Pisco", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Port", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Ricard", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Sambuca", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Sloe gin", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Southern Comfort", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Spiced rum", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Strawberry schnapps", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Sugar syrup", color = Color(1), isUnlocked = true),
            //IngredientEntity(name = "Sweet Vermouth", color = Color(1), isUnlocked = true),
        )
    }
}