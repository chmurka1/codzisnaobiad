package com.chmurka.codzisnaobiad.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.chmurka.codzisnaobiad.database.RecipeEvent
import com.chmurka.codzisnaobiad.database.RecipeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    toggleDrawer: () -> Unit,
    onRecipeClick: () -> Unit,
    onAddRecipeClick: () -> Unit,
    databaseState : RecipeState,
    onDatabaseEvent : (RecipeEvent) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(
            scrollBehavior = scrollBehavior,
            toggleDrawer = toggleDrawer
        )},
        content = {
            RecipeList(innerPadding = it,
                recipeState = databaseState,
                onEvent = onDatabaseEvent,
                onRecipeClick = { onRecipeClick() },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddRecipeClick() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    )
}