package com.example.saftyapp.model.database

import android.util.Log
import com.example.saftyapp.model.objects.ArchiveEntry
import com.example.saftyapp.model.objects.Ingredient
import com.example.saftyapp.model.objects.Recipe
import com.example.saftyapp.model.objects.UserData
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.database.entities.ArchiveRecipeCrossRef
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
                    color = e.recipe.backGroundColor
                )
            }

            return recipes
        }

        suspend fun getIngredientRecommendations(ingredients: List<Ingredient>): List<Ingredient> {
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

        suspend fun getRecipeRecommendations(ingredients: List<Ingredient>): List<Recipe> {
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

        // Instruction Screen
        suspend fun getRecipeByName(name: String): Recipe {
            val entity = recipeDao.getRecipeByName(name)
            return Recipe(
                name = entity.recipe.name,
                instructions = entity.recipe.instructions,
                isCustom = entity.recipe.isCustom,
                isAlcoholic = entity.recipe.isAlcoholic,
                thumbnail = entity.recipe.thumbnail,
                keyIngredients = entity.ingredients.map { i ->
                    Ingredient(
                        name = i.name,
                        iconFilePath = i.iconFilePath,
                        color = i.color,
                        isUnlocked = i.isUnlocked,
                    )
                },
                color = entity.recipe.backGroundColor,
                allIngredients = entity.recipe.allIngredients.split(","),
            )
        }

        suspend fun addRecipe(recipe: Recipe) {
            Log.d("Database","Adding Recipe: "+recipe.name)
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
                    recipeID = recipeDao.getRecipeByName(recipe.name).recipe.id,
                    ingredientID = recipeDao.getIngredientByName(i.name).id,
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

        suspend fun setTitle(title: Boolean) {
            userDao.updateTitle(title)
        }

        suspend fun increaseLvL() {
            userDao.increaseLvL()
            resetXP()
            updateTargetXP()
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
                        name = e.recipe.name,
                        instructions = e.recipe.instructions,
                        thumbnail = e.recipe.thumbnail,
                        isCustom = e.recipe.isCustom,
                        isAlcoholic = e.recipe.isAlcoholic,
                        keyIngredients = recipeDao.getRecipeByName(e.recipe.name).ingredients.map { i ->
                            Ingredient(
                                name = i.name,
                                iconFilePath = i.iconFilePath,
                                color = i.color,
                                isUnlocked = i.isUnlocked,
                            )
                        },
                        allIngredients = e.recipe.allIngredients.split(","),
                        color = e.recipe.backGroundColor
                    ),
                    imageFilePath = e.archive.imageFilePath,
                    date = e.archive.date
                )
            }
        }

        suspend fun addArchiveEntry(archiveEntry: ArchiveEntry){
            val aEntity = ArchiveEntryEntity(
                imageFilePath = archiveEntry.imageFilePath,
                date = archiveEntry.date
            )
            archiveDao.insertArchiveEntry(aEntity)

            val crossRef = ArchiveRecipeCrossRef(
                recipeId = recipeDao.getRecipeByName(archiveEntry.recipe.name).recipe.id,
                archiveId = archiveDao.getArchiveIdByDate(archiveEntry.date)
            )
            archiveDao.insertArchiveCrossRef(crossRef)
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
                IngredientData.ingredients
            )
        }
    }

    suspend fun loadTestRecipes() {
        if(recipeDao.getRecipeNames().isNotEmpty())
            return

        Log.d("Database", "Loading test recipes")
        val recipes = RecipeData.recipes
        for (x in recipes) {
            RecipeFunctions().addRecipe(x)
        }
    }
}