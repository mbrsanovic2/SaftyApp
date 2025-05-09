package com.example.saftyapp.model.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ArchiveAndRecipe(
    @Embedded
    val recipeEntity: RecipeEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "recipeName"
    )
    val archive: ArchiveEntryEntity
)
