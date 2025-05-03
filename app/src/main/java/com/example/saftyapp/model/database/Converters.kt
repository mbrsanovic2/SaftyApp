package com.example.saftyapp.model.database

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromColor(color: Color):String{
        return color.value.toULong().toString()
    }

    @TypeConverter
    fun toColor(colorString: String):Color{
        return Color(colorString.toULong())
    }

    @TypeConverter
    fun fromDate(date: Date):String{
        return date.time.toString()
    }

    @TypeConverter
    fun toDate(dateLong: String):Date{
        return Date(dateLong.toLong())
    }
}