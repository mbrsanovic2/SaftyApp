package com.example.saftyapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.database.entities.ArchiveRecipeCrossRef
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.database.entities.MeasureEntity
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.UserEntity

@Database(
    entities = [RecipeEntity::class, ArchiveRecipeCrossRef::class, IngredientEntity::class, UserEntity::class, ArchiveEntryEntity::class, MeasureEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun archiveDao(): ArchiveDao

    companion object {
        @Volatile
        private var Instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
                    .fallbackToDestructiveMigration(true).build().also { Instance = it }
            }
        }
    }
}