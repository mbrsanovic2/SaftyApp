package com.example.saftyapp.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.database.entities.ArchiveRecipeCrossRef
import com.example.saftyapp.model.database.entities.ArchiveWithRecipe
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

    @Query("SELECT * FROM recipeIngredientCrossRef WHERE recipeID = :rID AND ingredientID = :iID")
    suspend fun getMeasure(rID: Int, iID: Int): RecipeIngredientCrossRef

    @Query("SELECT * FROM ingredients")
    suspend fun getAllIngredients(): List<IngredientEntity>

    @Query("SELECT name FROM recipes")
    suspend fun getRecipeNames(): List<String>

    @Query("SELECT * FROM ingredients WHERE name = :name ")
    suspend fun getIngredientByName(name: String): IngredientEntity

    @Query("SELECT * FROM recipes WHERE name = :name")
    suspend fun getRecipeByName(name: String): RecipeWithIngredientsEntity

    @Query("SELECT * FROM recipes WHERE id IN (SELECT DISTINCT recipeID FROM recipeIngredientCrossRef WHERE ingredientID = :iID)")
    suspend fun getRecipeByIngredient(iID: Int): List<RecipeWithIngredientsEntity>

    @Query("SELECT recipeID FROM recipeIngredientCrossRef WHERE ingredientID = :iID")
    suspend fun getRecipeIDsFromIngredients(iID: Int): List<Int>

    @Query("SELECT * FROM ingredients WHERE id = :id")
    suspend fun getIngredientById(id: Int): IngredientEntity

    @Query("SELECT ingredientID FROM recipeIngredientCrossRef WHERE recipeID IN (:rID)")
    suspend fun getIngredientIDsFromRecipes(rID: List<Int>): List<Int>

    @Query("SELECT * FROM ingredients WHERE id IN (:iID)")
    suspend fun getRecommendations(iID: List<Int>): List<IngredientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasures(ref: List<RecipeIngredientCrossRef>)

    //TODO- getLockedIngredients
    //TODO- unlockIngredients
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
    @Query("SELECT * FROM archiveEntry")
    suspend fun getAllArchiveEntries(): List<ArchiveWithRecipe>

    @Insert
    suspend fun insertArchiveEntry(archiveEntryEntity: ArchiveEntryEntity)

    @Insert
    suspend fun insertArchiveCrossRef(archiveRecipeCrossRef: ArchiveRecipeCrossRef)

    @Query("SELECT id FROM archiveEntry WHERE date = :date")
    suspend fun getArchiveIdByDate(date: Date): Int
}