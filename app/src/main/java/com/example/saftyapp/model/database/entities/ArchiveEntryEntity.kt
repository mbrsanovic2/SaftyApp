package com.example.saftyapp.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "archiveEntry")
data class ArchiveEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageFilePath: String
)
