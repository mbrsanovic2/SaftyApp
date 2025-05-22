package com.example.saftyapp.model.database

import android.util.Log
import com.example.saftyapp.model.api.APIInterface
import com.example.saftyapp.model.api.CocktailDB
import com.example.saftyapp.model.api.DrinkDetails
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.objects.ArchiveEntry
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.model.objects.UserData
import com.example.saftyapp.model.database.entities.RecipeIngredientCrossRef
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.UserEntity
import com.example.saftyapp.model.database.staticdata.IngredientData
import com.example.saftyapp.model.database.staticdata.RecipeData
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val userDao: UserDao,
    private val archiveDao: ArchiveDao
) {
    /**
     * All database interactions for recipes and ingredients
     */
    inner class RecipeFunctions {
        // Returns all Ingredients
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

        // Gets all available Recipes
        suspend fun getAllRecipes(): List<Recipe> {
            val entities = recipeDao.getRecipesWithIngredients()
            val recipes: List<Recipe> = entities.map { e ->
                Recipe(
                    name = e.recipe.name,
                    instructions = e.recipe.instructions,
                    thumbnail = e.recipe.thumbnail,
                    isCustom = e.recipe.isCustom,
                    isAlcoholic = e.recipe.isAlcoholic,
                    keyIngredients = e.ingredients.map { i ->
                        Ingredient(name = i.name)
                    },
                    allIngredients = e.recipe.allIngredients.split(","),
                    color = e.recipe.backGroundColor,
                    hasBeenPhotoScored = e.recipe.hasPhotoScore,
                    hasBeenScored = e.recipe.hasBeenScored
                )
            }

            return recipes
        }

        suspend fun getIngredientRecommendations(ingredients: List<Ingredient>): List<Ingredient> {
            val input = ingredients.map { x ->
                x.name
            }
            val recipes = recipeDao.getRecipesByIngredients(input)

            val ingFilter = recipes.filter { r ->
                r.ingredients.map { i ->
                    i.name
                }.containsAll(input)
            }.map { tmp -> tmp.recipe.name }

            val filtered = recipeDao.getUnlockedRecipes(ingFilter)

            //Get all ingredients used by recipes and remove input ingredients
            val retIng = recipeDao.getIngredientsByRecipe(filtered).map { e ->
                Ingredient(
                    name = e.name,
                    isUnlocked = e.isUnlocked,
                    iconFilePath = e.iconFilePath,
                    color = e.color,
                    recentlyUnlocked = false,
                )
            } - ingredients.toSet()
            return retIng
        }

        suspend fun getRecipeRecommendations(ingredients: List<Ingredient>): List<Recipe> {
            val input = ingredients.map { x ->
                x.name
            }
            val recipes = recipeDao.getRecipesByIngredients(input)

            val ingFilter = recipes.filter { r ->
                r.ingredients.map { i ->
                    i.name
                }.containsAll(input)
            }

            return ingFilter.map { r ->
                Recipe(name = r.recipe.name)
            }
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

        suspend fun addRecipe(recipe: Recipe) {
            recipeDao.insertRecipe(
                RecipeEntity(
                    name = recipe.name,
                    isCustom = recipe.isCustom,
                    isAlcoholic = recipe.isAlcoholic,
                    instructions = recipe.instructions,
                    thumbnail = recipe.thumbnail,
                    backGroundColor = recipe.color,
                    allIngredients = recipe.allIngredients.joinToString(",")
                )
            )

            val crossRefs = recipe.keyIngredients.map { i ->
                RecipeIngredientCrossRef(
                    recipeName = recipeDao.getRecipeByName(recipe.name).recipe.name,
                    ingredientName = recipeDao.getIngredientByName(i.name).name,
                )
            }

            recipeDao.insertRecipeCrossIngredients(crossRefs)
        }

        suspend fun unlockRandomIngredients(): List<Ingredient> {
            val entities = recipeDao.getFiveLockedIngredients()
            recipeDao.unlockIngredients(entities.map { e -> e.name })

            val ingredients: List<Ingredient> = entities.map { e ->
                Ingredient(
                    name = e.name,
                    iconFilePath = e.iconFilePath,
                    color = e.color,
                    isUnlocked = true,
                    recentlyUnlocked = true
                )
            }

            return ingredients
        }

        suspend fun unlockAllIngredients(): List<Ingredient> {
            val entities = recipeDao.getAllLockedIngredients()
            recipeDao.unlockAllIngredients()
            return entities.map { e ->
                Ingredient(
                    name = e.name,
                    isUnlocked = true,
                    color = e.color,
                    iconFilePath = e.iconFilePath,
                    recentlyUnlocked = true
                )
            }
        }

        suspend fun finishRecipe(recipe: String) {
            recipeDao.scoreRecipe(recipe)
        }

        suspend fun photoScoreRecipe(recipe: String) {
            recipeDao.scorePhoto(recipe)
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

        suspend fun setTitle(title: Boolean) {
            userDao.updateTitle(title)
        }

        suspend fun increaseLvL(): List<Ingredient> {
            userDao.increaseLvL()
            val ret = if (userDao.getUser()?.hasTitle == false) {
                RecipeFunctions().unlockRandomIngredients()
            } else {
                emptyList()
            }
            resetXP()
            updateTargetXP()

            return ret
        }

        private suspend fun resetXP() {
            userDao.resetXP()
        }

        private suspend fun updateTargetXP() {
            userDao.updateTarget()
        }
    }

    /**
     * All database interactions for the archive
     */
    inner class ArchiveFunctions {
        suspend fun getArchive(): List<ArchiveEntry> {
            val entities = archiveDao.getAllArchiveEntries()
            return entities.map { e ->
                ArchiveEntry(
                    recipe = Recipe(
                        name = e.recipeEntity.name,
                        thumbnail = e.recipeEntity.thumbnail,
                    ),
                    imageFilePath = e.archive.imageFilePath,
                    date = e.archive.date
                )
            }
        }

        suspend fun addArchiveEntry(archiveEntry: ArchiveEntry) {
            if (!archiveDao.lookupRecipe(archiveEntry.recipe.name)) {
                val aEntity = ArchiveEntryEntity(
                    imageFilePath = archiveEntry.imageFilePath,
                    date = archiveEntry.date,
                    recipeName = archiveEntry.recipe.name
                )

                recipeDao.scoreRecipe(archiveEntry.recipe.name)
                archiveDao.insertArchiveEntry(aEntity)
            }
        }

        suspend fun addCustomPhoto(filePath: String, recipeName: String) {
            archiveDao.setImage(filePath, recipeName)
            recipeDao.scorePhoto(recipeName)
        }
    }


    suspend fun loadDefaultData() {
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
                IngredientData.ingredients
            )
        }
    }

    suspend fun loadTestRecipes() {
        if (recipeDao.getRecipeNames().isNotEmpty())
            return

        Log.i("Database", "Loading test recipes")
        val recipes = RecipeData.recipes
        for (x in recipes) {
            RecipeFunctions().addRecipe(x)
        }
    }

    suspend fun getAPIData(): List<Recipe> {
        val api = CocktailDB.getInstance().create(APIInterface::class.java)
        val ingredientNames = RecipeFunctions().getAllIngredients().map { x -> x.name }
        val response = mutableSetOf<String>()

        //Get ids from API by ingredient
        for (ingredient in ingredientNames) {
            var tries = 10

            //Try getting data, max 5 times
            while (true) {
                val tmp = api.getDrinksByIngredient(ingredient)
                if (tmp.code() == 429) {
                    delay(2000)
                    tries--
                } else if (tries <= 0) {
                    Log.i("API", "Couldn't get: " + ingredient)
                    break
                } else {
                    tmp.body()!!.drinks.forEach { drink ->
                        response.add(drink.idDrink)
                    }
                    break
                }
            }
        }

        val allDrinks = mutableListOf<Recipe>()
        for (drinkID in response) {
            var tries = 10
            while (true) {
                val tmp = api.getDrinkDetails(drinkID.toInt())
                if (tmp.code() == 429) {
                    delay(2000)
                    tries--
                } else if (tries <= 0) {
                    Log.i("API", "Couldn't get: " + drinkID)
                    break
                } else {
                    allDrinks.add(convertAPItoRecipe(tmp.body()!!.drinks[0]))
                    break
                }
            }
        }
        return allDrinks
    }

    private suspend fun convertAPItoRecipe(drink: DrinkDetails): Recipe {
        return Recipe(
            name = drink.strDrink,
            isCustom = false,
            instructions = drink.strInstructions,
            thumbnail = drink.strDrinkThumb,
            hasBeenScored = false,
            hasBeenPhotoScored = false,
            keyIngredients = getKeyIngredientsFromDrink(drink),
            allIngredients = getAllIngredientsFromDrink(drink),
            isAlcoholic = drink.strAlcoholic == "Alcoholic",
            color = null
        )
    }

    private fun getAllIngredientsFromDrink(drink: DrinkDetails): List<String> {
        val result = listOf(
            drink.strIngredient1 + ": " + drink.strMeasure1,
            drink.strIngredient2 + ": " + drink.strMeasure2,
            drink.strIngredient3 + ": " + drink.strMeasure3,
            drink.strIngredient4 + ": " + drink.strMeasure4,
            drink.strIngredient5 + ": " + drink.strMeasure5,
            drink.strIngredient6 + ": " + drink.strMeasure6,
            drink.strIngredient7 + ": " + drink.strMeasure7,
            drink.strIngredient8 + ": " + drink.strMeasure8,
            drink.strIngredient9 + ": " + drink.strMeasure9,
            drink.strIngredient10 + ": " + drink.strMeasure10,
            drink.strIngredient11 + ": " + drink.strMeasure11,
            drink.strIngredient12 + ": " + drink.strMeasure12,
            drink.strIngredient13 + ": " + drink.strMeasure13,
            drink.strIngredient14 + ": " + drink.strMeasure14,
            drink.strIngredient15 + ": " + drink.strMeasure15,
        )
        return result
    }

    private suspend fun getKeyIngredientsFromDrink(drink: DrinkDetails): List<Ingredient> {
        val knownIngredients = RecipeFunctions().getAllIngredients().map { x -> x.name }
        val toTest = listOf(
            drink.strIngredient1,
            drink.strIngredient2,
            drink.strIngredient3,
            drink.strIngredient4,
            drink.strIngredient5,
            drink.strIngredient6,
            drink.strIngredient7,
            drink.strIngredient8,
            drink.strIngredient9,
            drink.strIngredient10,
            drink.strIngredient11,
            drink.strIngredient12,
            drink.strIngredient13,
            drink.strIngredient14,
            drink.strIngredient15
        )
        val result = toTest.filter { x -> knownIngredients.contains(x) }.map { x ->
            RecipeFunctions().getIngredient(x!!)
        }

        return result
    }
}