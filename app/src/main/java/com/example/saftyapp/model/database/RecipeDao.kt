package com.example.saftyapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.saftyapp.model.database.entities.ArchiveAndRecipe
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.database.entities.RecipeIngredientCrossRef
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.RecipeWithIngredientsEntity
import com.example.saftyapp.model.database.entities.UserEntity
import java.util.Date

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    suspend fun getRecipesWithIngredients(): List<RecipeWithIngredientsEntity>

    @Query("SELECT * FROM recipeIngredientCrossRef WHERE recipeName = :rName AND ingredientName = :iName")
    suspend fun getMeasure(rName: String, iName: String): RecipeIngredientCrossRef

    @Query("SELECT * FROM ingredients")
    suspend fun getAllIngredients(): List<IngredientEntity>

    @Query("SELECT name FROM recipes")
    suspend fun getRecipeNames(): List<String>

    @Query("SELECT * FROM ingredients WHERE name = :name ")
    suspend fun getIngredientByName(name: String): IngredientEntity

    @Query("SELECT * FROM recipes WHERE name = :name")
    suspend fun getRecipeByName(name: String): RecipeWithIngredientsEntity

    @Query("SELECT * FROM recipes WHERE name IN (SELECT DISTINCT recipeName FROM recipeIngredientCrossRef WHERE ingredientName = :iName)")
    suspend fun getRecipeByIngredient(iName: String): List<RecipeWithIngredientsEntity>

    @Query("SELECT * FROM recipes WHERE name IN(SELECT DISTINCT recipeName FROM recipeIngredientCrossRef WHERE ingredientName IN (SELECT name FROM ingredients WHERE name IN (:ingredientNames)))")
    suspend fun getRecipesByIngredients(ingredientNames: List<String>): List<RecipeWithIngredientsEntity>

    @Query("SELECT * FROM ingredients WHERE name IN (SELECT DISTINCT ingredientName FROM recipeIngredientCrossRef WHERE recipeName IN ( :recipes ))")
    suspend fun getIngredientsByRecipe(recipes: List<String>): List<IngredientEntity>

    @Query(
        "SELECT DISTINCT recipeName FROM recipeIngredientCrossRef r WHERE NOT EXISTS " +
                "(SELECT 1 FROM ingredients WHERE name IN " +
                "(SELECT ingredientName FROM recipeIngredientCrossRef WHERE recipeName = r.recipeName) " +
                "AND isUnlocked = 0) AND recipeName IN (:recipes)"
    )
    suspend fun getUnlockedRecipes(recipes: List<String>): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeCrossIngredients(ref: List<RecipeIngredientCrossRef>)

    @Query("SELECT * FROM ingredients WHERE isUnlocked = 0 ORDER BY RANDOM() LIMIT 5")
    suspend fun getFiveLockedIngredients(): List<IngredientEntity>

    @Query("UPDATE ingredients SET isUnlocked = 1 WHERE name IN (:ingredients)")
    suspend fun unlockIngredients(ingredients: List<String>)

    @Query("SELECT * FROM ingredients WHERE isUnlocked = 0")
    suspend fun getAllLockedIngredients(): List<IngredientEntity>

    @Query("UPDATE ingredients SET isUnlocked = 1")
    suspend fun unlockAllIngredients()

    @Query("UPDATE recipes SET hasBeenScored = 1 WHERE name = :recipe")
    suspend fun scoreRecipe(recipe: String)

    @Query("UPDATE recipes SET hasPhotoScore = 1 WHERE name = :recipe")
    suspend fun scorePhoto(recipe: String)
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = 1")
    suspend fun getUser(): UserEntity?

    @Query("UPDATE user SET hasTitle = :title WHERE id = 1")
    suspend fun updateTitle(title: Boolean)

    @Query("UPDATE user SET currentLvL = currentLvL + 1 WHERE id = 1")
    suspend fun increaseLvL()

    @Query("UPDATE user SET currentXP = currentXP + :xp WHERE id = 1")
    suspend fun increaseXP(xp: Int)

    @Query("UPDATE user SET currentXP = 0 WHERE id = 1")
    suspend fun resetXP()

    @Query("UPDATE user SET currentLvL = :lvl WHERE id = 1")
    suspend fun setLvL(lvl: Int)

    @Query("UPDATE user SET targetXP = currentLvL * 10 WHERE id = 1")
    suspend fun updateTarget()
}

@Dao
interface ArchiveDao {
    @Insert
    suspend fun insertArchiveEntry(archiveEntryEntity: ArchiveEntryEntity)

    @Query("SELECT * FROM recipes WHERE name IN (SELECT recipeName FROM archiveEntry)")
    suspend fun getAllArchiveEntries(): List<ArchiveAndRecipe>

    @Query("SELECT id FROM archiveEntry WHERE date = :date")
    suspend fun getArchiveIdByDate(date: Date): Int

    @Query("UPDATE archiveEntry SET imageFilePath = :filePath WHERE recipeName = :recipe")
    suspend fun setImage(filePath: String, recipe: String)

    @Query("SELECT 1 FROM archiveEntry WHERE recipeName = :recipeName")
    suspend fun lookupRecipe(recipeName: String): Boolean
}