package com.example.saftyapp.model.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ArchiveWithRecipe(
    @Embedded
    val archive: ArchiveEntryEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ArchiveRecipeCrossRef::class,
            parentColumn = "archiveId",
            entityColumn = "recipeId"
        )
    )
    val recipe: RecipeEntity
)
