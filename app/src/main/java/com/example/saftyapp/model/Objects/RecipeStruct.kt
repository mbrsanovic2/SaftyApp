package com.example.saftyapp.model.Objects

data class RecipeStruct(
    val name:String,
    val instruction:String,
    val ingredients:List<Ingredient>,
    val thumbnail:String?=null,
    val isCustom:Boolean,
    val isAlcoholic:Boolean,
)
