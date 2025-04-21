package com.example.saftyapp.model.database

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromColor(color: Color):String{
        return color.value.toULong().toString()
    }

    @TypeConverter
    fun toColor(colorString: String):Color{
        return Color(colorString.toULong())
    }
}