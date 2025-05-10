package com.example.saftyapp.model

import com.example.saftyapp.model.database.staticdata.RecipeData
import com.example.saftyapp.model.objects.ArchiveEntry
import java.text.SimpleDateFormat
import java.util.Locale

fun getTestArchive(): List<ArchiveEntry> {
    val formatter = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
    return listOf(
        ArchiveEntry(recipe = RecipeData.recipes[0], date = formatter.parse("01.02.25")!!),
        ArchiveEntry(recipe = RecipeData.recipes[3], date = formatter.parse("01.02.25")!!),
        ArchiveEntry(recipe = RecipeData.recipes[0], date = formatter.parse("01.02.25")!!),
        ArchiveEntry(recipe = RecipeData.recipes[1], date = formatter.parse("01.04.25")!!),
        ArchiveEntry(recipe = RecipeData.recipes[0], date = formatter.parse("01.06.24")!!),
        ArchiveEntry(recipe = RecipeData.recipes[2], date = formatter.parse("01.07.24")!!),
        ArchiveEntry(recipe = RecipeData.recipes[4], date = formatter.parse("01.04.25")!!),
        ArchiveEntry(recipe = RecipeData.recipes[2], date = formatter.parse("01.06.24")!!),
    )
}