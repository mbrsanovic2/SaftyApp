package com.example.saftyapp.model

data class Deprecated_Recipe(
    val name:String,
    val id:Int,
    val isCustom:Boolean,
    val alcoholic:Boolean,
    val instructions:String,
    val ingredients:Array<String>,
    val measures:Array<String>,
    val thumbnail:String?=null
)