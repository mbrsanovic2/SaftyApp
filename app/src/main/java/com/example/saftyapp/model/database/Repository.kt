package com.example.saftyapp.model.database

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.saftyapp.model.Objects.ArchiveEntry
import com.example.saftyapp.model.Objects.Ingredient
import com.example.saftyapp.model.Objects.RecipeStruct
import com.example.saftyapp.model.Objects.UserData
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.database.entities.MeasureEntity
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.UserEntity

class Repository(context: Context) {

    companion object {
        @Volatile
        private var instance: Repository? = null

        fun getInstance(context: Context): Repository {
            return instance ?: synchronized(this) {
                instance ?: Repository(context).also { instance = it }
            }
        }
    }

    private val db: RecipeDatabase = RecipeDatabase.getDatabase(context)
    private val recipeDao: RecipeDao = db.recipeDao()
    private val userDao: UserDao = db.userDao()
    private val archiveDao: ArchiveDao = db.archiveDao()

    /**
     * All database interactions for recipes and interactions
     */
    inner class RecipeFunctions {
        suspend fun getAllIngredients(): List<Ingredient> {
            val entities = recipeDao.getAllIngredients()
            val ingredients: List<Ingredient> = entities.map { e ->
                Ingredient(
                    name = e.name,
                    iconFilePath = e.iconFilePath,
                    color = e.color,
                    isUnlocked = e.isUnlocked
                )
            }
            return ingredients
        }

        suspend fun getAllFullRecipes(): List<RecipeStruct> {
            val entities = recipeDao.getRecipesWithIngredients()
            val recipes: List<RecipeStruct> = entities.map { e ->
                RecipeStruct(
                    name = e.recipe.name,
                    instructions = e.recipe.instructions,
                    thumbnail = e.recipe.thumbnail,
                    isCustom = e.recipe.isCustom,
                    isAlcoholic = e.recipe.isAlcoholic,
                    ingredients = e.ingredients.map { i ->
                        Ingredient(
                            name = i.name,
                            iconFilePath = i.iconFilePath,
                            color = i.color,
                            isUnlocked = i.isUnlocked,
                            measure = recipeDao.getMeasure(rID = e.recipe.id, iID = i.id).measure
                        )
                    },
                )
            }

            return recipes
        }

        suspend fun getAllRecipeNames(): List<String> {
            return recipeDao.getRecipeNames()
        }

        suspend fun getRecipeByIngredient(ingredient: Ingredient): List<RecipeStruct> {
            val iID = recipeDao.getIngredientByName(ingredient.name).id
            val entities = recipeDao.getRecipeByIngredient(iID)

            val recipes = entities.map { f ->
                RecipeStruct(
                    name = f.recipe.name,
                    instructions = f.recipe.instructions,
                    thumbnail = f.recipe.thumbnail,
                    isCustom = f.recipe.isCustom,
                    isAlcoholic = f.recipe.isAlcoholic,
                    ingredients = f.ingredients.map { i ->
                        Ingredient(
                            name = i.name,
                            iconFilePath = i.iconFilePath,
                            color = i.color,
                            isUnlocked = i.isUnlocked,
                            measure = recipeDao.getMeasure(rID = f.recipe.id, iID = i.id).measure
                        )
                    },
                )
            }

            return recipes
        }

        suspend fun getRecommendations(ingredients: List<Ingredient>): List<Ingredient> {
            //return ingredients that can be used as next ingredients
            val ingredientIDs = ingredients.map { x ->
                recipeDao.getIngredientByName(x.name).id
            }.toSet()
            val recipeIDsLists = ingredientIDs.map { x ->
                recipeDao.getRecipeIDsFromIngredients(x)
            }

            //get all rIDs in one list
            val allRIDs=recipeIDsLists.flatten().toSet().toList()

            //Check for overlap in rIDLists => recipes containing ingredients
            val overlapIngredients = allRIDs.filter{ x ->
                recipeIDsLists.all{ x in it}
            }

            //Get ingredientIds from overlap-recipes
            val allRecommendations = recipeDao.getIngredientIDsFromRecipes(overlapIngredients)

            //Filter the input ingredients from the recommendations
            val recommendations = (allRecommendations - ingredientIDs).map { x ->
                val y = recipeDao.getIngredientById(x)
                Ingredient(
                    name = y.name,
                    color = y.color,
                    isUnlocked = y.isUnlocked,
                    iconFilePath = y.iconFilePath,
                )
            }

            return recommendations
        }

        suspend fun getFinalRecommendations(ingredients: List<Ingredient>): List<RecipeStruct> {
            TODO()
        }

        suspend fun getIngredient(name: String): Ingredient {
            val entity = recipeDao.getIngredientByName(name)
            val ingredient = Ingredient(
                name = entity.name,
                iconFilePath = entity.iconFilePath,
                color = entity.color,
                isUnlocked = entity.isUnlocked,
            )
            return ingredient
        }

        suspend fun getRecipeByName(name: String): RecipeStruct {
            val entity = recipeDao.getRecipeByName(name)
            return RecipeStruct(
                name = entity.recipe.name,
                instructions = entity.recipe.instructions,
                isCustom = entity.recipe.isCustom,
                isAlcoholic = entity.recipe.isAlcoholic,
                thumbnail = entity.recipe.thumbnail,
                ingredients = entity.ingredients.map { i ->
                    Ingredient(
                        name = i.name,
                        iconFilePath = i.iconFilePath,
                        color = i.color,
                        isUnlocked = i.isUnlocked,
                        measure = recipeDao.getMeasure(rID = entity.recipe.id, iID = i.id).measure
                    )
                }
            )
        }

        suspend fun addRecipe(recipe: RecipeStruct) {
            Log.d("Database","Adding Recipe: "+recipe.name)
            recipeDao.insertRecipe(
                RecipeEntity(
                    name = recipe.name,
                    isCustom = recipe.isCustom,
                    isAlcoholic = recipe.isAlcoholic,
                    instructions = recipe.instructions,
                    thumbnail = recipe.thumbnail,
                )
            )

            val measures = recipe.ingredients.map { i ->
                MeasureEntity(
                    recipeID = recipeDao.getRecipeByName(recipe.name).recipe.id,
                    ingredientID = recipeDao.getIngredientByName(i.name).id,
                    measure = i.measure ?: "No Info"
                )
            }

            recipeDao.insertMeasures(measures)
        }

        suspend fun unlockRandomIngredients() {
            TODO()
        }

        suspend fun unlockAllIngredients() {
            TODO()
        }
    }

    /**
     * All database interactions for userData
     */
    inner class UserFunctions {
        suspend fun getUserData(): UserData {
            var entity = userDao.getUser()
            if (entity == null) {
                userDao.insertUser(
                    UserEntity(
                        currentXP = 0,
                        targetXP = 10,
                        currentLvL = 1,
                        hasTitle = false
                    )
                )
                entity = userDao.getUser()
            }
            val user = UserData(
                currentXP = entity!!.currentXP,
                targetXP = entity.targetXP,
                level = entity.currentLvL,
                isJUICY = entity.hasTitle
            )
            return user
        }

        suspend fun increaseXP(amount: Int) {
            userDao.increaseXP(amount)
        }

        suspend fun resetXP() {
            userDao.resetXP()
        }

        suspend fun updateTargetXP() {
            userDao.updateTarget()
        }

        suspend fun setTitle(title: Boolean) {
            userDao.updateTitle(title)
        }

        suspend fun increaseLvL() {
            userDao.increaseLvL()
            resetXP()
            updateTargetXP()
        }
    }

    /**
     * All database interactions for the archive
     */
    inner class ArchiveFunctions {
        @Deprecated("Not yet implemented", level = DeprecationLevel.ERROR)
        suspend fun getArchive(): List<ArchiveEntry> {
            TODO()
        }
    }


    suspend fun loadDefaultData() {
        Log.d("Database", "Loading default data")
        //Create default User
        if (userDao.getUser() == null) {
            userDao.insertUser(
                user = UserEntity(
                    currentXP = 0,
                    targetXP = 10,
                    currentLvL = 1,
                    hasTitle = false
                )
            )
        }

        //Create Ingredients
        if (recipeDao.getAllIngredients().isEmpty()) {
            recipeDao.insertIngredients(
                listOf(
                    IngredientEntity(
                        name = "Tabasco sauce",
                        color = Color.Red,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Light rum",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cranberry Juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Bourbon",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Vodka",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Gin",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Blended whiskey",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Tequila",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sweet Vermouth",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Apricot brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Triple sec",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Southern Comfort",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Orange bitters",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lemon vodka",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Dry Vermouth",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Dark rum",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Amaretto",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Tea",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Applejack",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Champagne",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Scotch",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Coffee liqueur",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Añejo rum",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Bitters",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sugar",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Kahlua",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Dubonnet Rouge",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lime juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Irish whiskey",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Apple brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Carbonated water",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cherry brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Creme de Cacao",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Grenadine",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Port",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Coffee brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Red wine",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Rum",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Grapefruit juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Ricard",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sherry",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cognac",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sloe gin",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Strawberry schnapps",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Apple juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Pineapple juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lemon juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sugar syrup",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Milk",
                        color = Color.White,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Strawberries",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Chocolate syrup",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Yoghurt",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Mango",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Ginger",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lime",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cantaloupe",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Berries",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Grapes",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Kiwi",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Tomato juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cocoa powder",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Chocolate",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Heavy cream",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Galliano",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Peach Vodka",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Ouzo",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Coffee",
                        color = Color.Black,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Spiced rum",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Water",
                        color = Color.Transparent,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Espresso",
                        color = Color.Black,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Angelica root",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Orange",
                        color = Color.Yellow,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cranberries",
                        color = Color.Red,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Johnnie Walker",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Apple cider",
                        color = Color.Yellow,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Everclear",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Egg yolk",
                        color = Color.Yellow,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Egg",
                        color = Color.Yellow,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Grape juice",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Peach nectar",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lemon",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Firewater",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lemonade",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Lager",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Whiskey",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Absolut Citron",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Pisco",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Irish cream",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Ale",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Chocolate liqueur",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Midori melon liqueur",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sambuca",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Cider",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Sprite",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "7-Up",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Blackberry brandy",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Peppermint schnapps",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Creme de Cassis",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Jack Daniels",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Baileys irish cream",
                        color = Color(1),
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Salt",
                        color = Color.White,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Apple",
                        color = Color.Red,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Light cream",
                        color = Color.White,
                        isUnlocked = true
                    ),
                    IngredientEntity(
                        name = "Worcestershire sauce",
                        color = Color.Black,
                        isUnlocked = true
                    )
                )
            )
        }
    }

    suspend fun loadTestRecipes() {
        Log.d("Database", "Loading test recipes")
        val recipes = listOf(
            RecipeStruct(
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
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/vsrupw1472405732.jpg"
            ),
            RecipeStruct(
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
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg"
            ),
            RecipeStruct(
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
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/xwqvur1468876473.jpg"
            ),
            RecipeStruct(
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
                thumbnail = "https://www.thecocktaildb.com/images/media/drink/abcpwr1504817734.jpg"
            ),
            RecipeStruct(
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
                )
            ),
            RecipeStruct(
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
                )
            )
        )
        for (x in recipes) {
            RecipeFunctions().addRecipe(x)
        }
    }
}