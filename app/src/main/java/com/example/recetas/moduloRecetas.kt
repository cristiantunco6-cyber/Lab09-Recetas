package com.example.recetas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun ScreenRecetas(navController: NavHostController, servicio: RecipeApiService) {
    val listaRecetas = remember { mutableStateListOf<RecipeModel>() }
    LaunchedEffect(Unit) {
        val respuesta = servicio.getRecipes()
        listaRecetas.addAll(respuesta.recipes)
    }
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(listaRecetas) { receta ->
            Card(
                onClick = { navController.navigate("receta/${receta.id}") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                AsyncImage(
                    model = receta.image,
                    contentDescription = receta.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = receta.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${receta.cuisine} - ${receta.difficulty}",
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Tiempo: ${receta.prepTimeMinutes + receta.cookTimeMinutes} min  |  Calificación: ${receta.rating}"
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenReceta(servicio: RecipeApiService, id: Int) {
    var receta by remember { mutableStateOf<RecipeModel?>(null) }
    LaunchedEffect(Unit) {
        receta = servicio.getRecipeById(id)
    }
    receta?.let { r ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = r.image,
                contentDescription = r.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = r.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${r.cuisine} - ${r.difficulty}",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DatoReceta("Preparación", "${r.prepTimeMinutes} min", Modifier.weight(1f))
                    DatoReceta("Cocción", "${r.cookTimeMinutes} min", Modifier.weight(1f))
                    DatoReceta("Porciones", "${r.servings}", Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DatoReceta("Calorías", "${r.caloriesPerServing}", Modifier.weight(1f))
                    DatoReceta("Calificación", "${r.rating}", Modifier.weight(1f))
                    DatoReceta("Reseñas", "${r.reviewCount}", Modifier.weight(1f))
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Ingredientes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                r.ingredients.forEach { ingrediente ->
                    Text(text = "- $ingrediente")
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Preparación paso a paso",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                r.instructions.forEachIndexed { indice, paso ->
                    Text(text = "${indice + 1}. $paso")
                }
            }
        }
    }
}

@Composable
fun DatoReceta(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = titulo, fontSize = 12.sp)
            Text(text = valor, fontWeight = FontWeight.Bold)
        }
    }
}