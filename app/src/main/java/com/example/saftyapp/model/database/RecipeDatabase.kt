package com.example.saftyapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.saftyapp.model.database.entities.ArchiveEntryEntity
import com.example.saftyapp.model.database.entities.IngredientEntity
import com.example.saftyapp.model.database.entities.RecipeEntity
import com.example.saftyapp.model.database.entities.RecipeIngredientCrossRef
import com.example.saftyapp.model.database.entities.UserEntity

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        UserEntity::class,
        ArchiveEntryEntity::class,
        RecipeIngredientCrossRef::class,
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun archiveDao(): ArchiveDao
}