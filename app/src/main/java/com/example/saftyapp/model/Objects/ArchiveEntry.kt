package com.example.saftyapp.model.Objects

import java.util.Date

data class ArchiveEntry(
    val recipe: Recipe,
    val imageFilePath: String,
    val date: Date
)
