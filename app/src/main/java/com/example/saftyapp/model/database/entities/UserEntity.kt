package com.example.saftyapp.model.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    val currentXP: Int,
    val targetXP: Int,
    val currentLvL: Int,
    val hasTitle: Boolean
)
