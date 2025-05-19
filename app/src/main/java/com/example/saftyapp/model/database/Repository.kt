package com.example.saftyapp.model.database

import android.util.Log
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
                        Ingredient(
                            name = i.name,
                            iconFilePath = i.iconFilePath,
                            color = i.color,
                            isUnlocked = i.isUnlocked,
                        )
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
            Log.d("Database", "------Starting Ingredient Recommendation------")
            val input = ingredients.map { x ->
                x.name
            }
            val recipes = recipeDao.getRecipesByIngredients(input)
            Log.d("Database", "Before filter " + recipes.map { r -> r.recipe.name }.toString())

            val ingFilter = recipes.filter { r ->
                r.ingredients.map { i ->
                    i.name
                }.containsAll(input)
            }.map { tmp -> tmp.recipe.name }
            Log.d("Database", "After filter " + ingFilter.toString())

            val filtered = recipeDao.getUnlockedRecipes(ingFilter)
            Log.d("Database", "After Unlocks " + filtered.toString())

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
            Log.d("Database", "Final\n" + retIng.map { i -> i.name } + "\n")
            return retIng
        }

        suspend fun getRecipeRecommendations(ingredients: List<Ingredient>): List<Recipe> {
            Log.d("Database", "------Starting Recipe Recommendation------")
            val input = ingredients.map { x ->
                x.name
            }
            val recipes = recipeDao.getRecipesByIngredients(input)
            Log.d("Database", "Before filter " + recipes.map { r -> r.recipe.name }.toString())

            val ingFilter = recipes.filter { r ->
                r.ingredients.map { i ->
                    i.name
                }.containsAll(input)
            }
            Log.d("Database", "After filter " + ingFilter.toString() + "\n")

            return ingFilter.map { r ->
                Recipe(
                    name = r.recipe.name,
                    thumbnail = r.recipe.thumbnail,
                    isCustom = r.recipe.isCustom,
                    isAlcoholic = r.recipe.isAlcoholic,
                    color = r.recipe.backGroundColor,
                    instructions = r.recipe.instructions,
                    allIngredients = r.recipe.allIngredients.split(","),
                    keyIngredients = r.ingredients.map { i ->
                        Ingredient(
                            name = i.name,
                            color = i.color,
                            isUnlocked = i.isUnlocked,
                            iconFilePath = i.iconFilePath,
                        )
                    },
                    hasBeenScored = r.recipe.hasBeenScored,
                    hasBeenPhotoScored = r.recipe.hasPhotoScore
                )
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
            Log.d("Database", "Adding Recipe: " + recipe.name)
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

            val measures = recipe.keyIngredients.map { i ->
                RecipeIngredientCrossRef(
                    recipeName = recipeDao.getRecipeByName(recipe.name).recipe.name,
                    ingredientName = recipeDao.getIngredientByName(i.name).name,
                )
            }

            recipeDao.insertRecipeCrossIngredients(measures)
        }

        suspend fun unlockRandomIngredients(): List<Ingredient> {
            val entities = recipeDao.getFiveLockedIngredients()
            recipeDao.unlockIngredients(entities.map { e -> e.name })

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

        suspend fun unlockAllIngredients(): List<Ingredient> {
            val entities = recipeDao.getAllLockedIngredients()
            recipeDao.unlockAllIngredients()
            return entities.map { e ->
                Ingredient(
                    name = e.name,
                    isUnlocked = e.isUnlocked,
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

            Log.i("Database", "Unlocked Ingredients:\n" + ret.map { x -> x.name }.toString())

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
                        instructions = e.recipeEntity.instructions,
                        thumbnail = e.recipeEntity.thumbnail,
                        isCustom = e.recipeEntity.isCustom,
                        isAlcoholic = e.recipeEntity.isAlcoholic,
                        keyIngredients = recipeDao.getRecipeByName(e.recipeEntity.name).ingredients.map { i ->
                            Ingredient(
                                name = i.name,
                                iconFilePath = i.iconFilePath,
                                color = i.color,
                                isUnlocked = i.isUnlocked,
                            )
                        },
                        allIngredients = e.recipeEntity.allIngredients.split(","),
                        color = e.recipeEntity.backGroundColor,
                        hasBeenScored = e.recipeEntity.hasBeenScored,
                        hasBeenPhotoScored = e.recipeEntity.hasPhotoScore
                    ),
                    imageFilePath = e.archive.imageFilePath,
                    date = e.archive.date
                )
            }
        }

        suspend fun addArchiveEntry(archiveEntry: ArchiveEntry) {
            if(!archiveDao.lookupRecipe(archiveEntry.recipe.name)){
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
        Log.i("Database", "Loading default data")
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
}