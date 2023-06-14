package com.chmurka.codzisnaobiad.composables

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chmurka.codzisnaobiad.database.RecipeEvent
import com.chmurka.codzisnaobiad.database.RecipeState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeView(
    goBack : () -> Unit,
    databaseState: RecipeState,
    onDatabaseEvent: (RecipeEvent) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MinorTopBar(
                scrollBehavior = scrollBehavior,
                goBack = goBack,
                name = databaseState.currentRecipe.name
            )
        },
        content = {
            LazyColumn(
                Modifier.padding(it).padding(10.dp)
            )
            {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (databaseState.currentRecipe.imageUri != null) {
                            AsyncImage(
                                model = Uri.fromFile(File(databaseState.currentRecipe.imageUri)),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Icon(
                                Icons.Filled.NoPhotography,
                                contentDescription = null,
                                Modifier.size(200.dp)
                            )
                        }
                    }
                }
                item {
                    Column(
                        Modifier.padding(10.dp)
                    ) {
                        /*Text(text = "Składniki", Modifier.padding(vertical = 10.dp), style = MaterialTheme.typography.titleLarge)
                        for (item in arrayListOf("składnik1", "składnik2", "składnik3")) {
                            Text(text = item, Modifier.padding(2.dp))
                        }*/
                        Text(
                            text = "Opis",
                            Modifier.padding(vertical = 10.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        //for (item in arrayListOf("krok1", "krok2", "krok3")) {
                        Text(text = databaseState.currentRecipe.desc, Modifier.padding(2.dp))
                        //}
                    }
                }
            }
        },
        /*floatingActionButton = {
            val context = LocalContext.current
            fun showToast() {
                Toast.makeText(context, "To be implemented...", Toast.LENGTH_SHORT).show()
            }
            FloatingActionButton(
                onClick = { showToast() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Filled.Edit, contentDescription = null)
            }
        }*/
    )
}