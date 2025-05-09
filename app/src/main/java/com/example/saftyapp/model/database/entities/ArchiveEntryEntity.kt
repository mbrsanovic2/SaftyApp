package com.example.saftyapp.model.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "archiveEntry")
data class ArchiveEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageFilePath: String?,
    val date: Date,
    val recipeName: String
)
