package com.example.saftyapp.model.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRecipeDatabase(@ApplicationContext context: Context): RecipeDatabase {
        return Room.databaseBuilder(
            context,
            RecipeDatabase::class.java,
            "recipe_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRecipeDao(db: RecipeDatabase): RecipeDao = db.recipeDao()

    @Provides
    fun provideUserDao(db: RecipeDatabase): UserDao = db.userDao()

    @Provides
    fun provideArchiveDao(db: RecipeDatabase): ArchiveDao = db.archiveDao()
}