package com.example.recetas

import com.google.gson.annotations.SerializedName

data class RecipesResponse(
    @SerializedName("recipes") val recipes: List<RecipeModel>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip") val skip: Int,
    @SerializedName("limit") val limit: Int
)