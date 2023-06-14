package com.chmurka.codzisnaobiad.composables

import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.chmurka.codzisnaobiad.database.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.sharp.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ProductCard(
    product: Product,
    onClick : () -> Unit,
    onDatabaseEvent : (ProductEvent) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                text = product.name,
                style = MaterialTheme.typography.titleLarge
            )
        },
        trailingContent = {
            Row() {
                IconButton(onClick = { onDatabaseEvent(ProductEvent.DeleteProduct(product)) }) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                }
            }
        },
        modifier = Modifier.clickable {
            onDatabaseEvent(ProductEvent.SetCurrentProduct(product))
            onClick()
        }
    )
}

@Composable
fun ProductList(
    innerPadding: PaddingValues = PaddingValues(),
    productState : ProductState,
    onProductClick: () -> Unit,
    onEvent : (ProductEvent) -> Unit,
) {
    LazyColumn(
        contentPadding = innerPadding,
    ) {
        items(productState.products) {
            ProductCard(product = it, onClick = { onProductClick() }, onDatabaseEvent = { e -> onEvent(e)})
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListView(
    toggleDrawer: () -> Unit,
    onProductClick: () -> Unit,
    onAddProductClick: () -> Unit,
    databaseState : ProductState,
    onDatabaseEvent : (ProductEvent) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(
            scrollBehavior = scrollBehavior,
            toggleDrawer = toggleDrawer
        )},
        content = {
            ProductList(innerPadding = it,
                productState = databaseState,
                onEvent = onDatabaseEvent,
                onProductClick = { onProductClick() },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddProductClick() },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    )
}