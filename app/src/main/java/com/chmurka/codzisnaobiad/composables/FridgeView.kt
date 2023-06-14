package com.chmurka.codzisnaobiad.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.database.*

@Composable
fun IngredientCard(
    modifier : Modifier = Modifier,
    fridgeProduct: FridgeProduct,
    product: Product,
    name : String,
    date : String,
    amount : Float,
    onDatabaseEvent : (FridgeProductEvent) -> Unit,
    onProductDatabaseEvent : (ProductEvent) -> Unit,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = "$name ($amount g)",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        trailingContent = {
            Row {
                IconButton(onClick = { onDatabaseEvent(FridgeProductEvent.DeleteFridgeProduct(fridgeProduct)) }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }
        },
        modifier = modifier.clickable {
            onProductDatabaseEvent(ProductEvent.SetCurrentProduct(product))
            onClick()
        }
    )
}

data class IngredientInfo(
    val name : String,
    val date : String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeView(
    toggleDrawer: () -> Unit,
    onAddIngredientClick: () -> Unit,
    databaseState: FridgeProductState,
    onDatabaseEvent: (FridgeProductEvent) -> Unit,
    onIngredientClick: () -> Unit,
    onProductDatabaseEvent: (ProductEvent) -> Unit
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(
            scrollBehavior = scrollBehavior,
            toggleDrawer = toggleDrawer
        )},
        content = {
            Column {
                Divider()
                LazyColumn(Modifier.padding(it)) {
                    items(databaseState.fridgeProducts.toList()) {
                            IngredientCard(
                                fridgeProduct = it.first,
                                product = it.second,
                                name = it.second.name,
                                date = it.first.expirationDate?:"?",
                                amount = it.first.amount_g,
                                onDatabaseEvent = onDatabaseEvent,
                                onProductDatabaseEvent = onProductDatabaseEvent,
                                onClick = onIngredientClick
                            )
                            Divider()
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddIngredientClick() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    )
}