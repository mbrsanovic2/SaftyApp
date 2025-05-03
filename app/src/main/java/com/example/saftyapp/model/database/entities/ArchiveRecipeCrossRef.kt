package com.example.saftyapp.model.database.entities

import androidx.room.Entity

@Entity(tableName = "archiveCrossRef", primaryKeys = ["recipeId","archiveId"])
data class ArchiveRecipeCrossRef(
    val recipeId: Int,
    val archiveId: Int
)
