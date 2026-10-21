package com.example.recetas

import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("recipes")
    suspend fun getRecipes(): RecipesResponse

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): RecipeModel
}