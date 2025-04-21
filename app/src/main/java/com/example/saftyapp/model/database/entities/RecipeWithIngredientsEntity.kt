package com.example.saftyapp.model.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecipeWithIngredientsEntity(
    @Embedded
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MeasureEntity::class,
            parentColumn = "recipeID",
            entityColumn = "ingredientID"
        )
    )
    val ingredients: List<IngredientEntity>
)
