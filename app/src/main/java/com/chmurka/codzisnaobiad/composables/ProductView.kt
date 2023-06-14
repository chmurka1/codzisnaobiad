package com.chmurka.codzisnaobiad.composables

import com.chmurka.codzisnaobiad.database.ProductEvent
import com.chmurka.codzisnaobiad.database.ProductState

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductView(
    goBack : () -> Unit,
    databaseState: ProductState,
    onDatabaseEvent: (ProductEvent) -> Unit,
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MinorTopBar(
                scrollBehavior = scrollBehavior,
                goBack = goBack,
                name = databaseState.currentProduct.name
            )
        },
        content = {
            Column(
                Modifier.padding(it).padding(10.dp)
            )
            {
                ListItem(
                    headlineContent = { Text("${stringResource(R.string.energy)} [kcal / 100 g]") },
                    trailingContent = {
                        Text(style = MaterialTheme.typography.labelMedium,
                            text = databaseState.currentProduct.energyKcal.toString())
                    }
                )
                Divider()
                ListItem(
                    headlineContent = { Text("${stringResource(R.string.proteins)} [g / 100 g]") },
                    trailingContent = {
                        Text(style = MaterialTheme.typography.labelMedium,
                            text = databaseState.currentProduct.proteins100g.toString())
                    }
                )
                Divider()
                ListItem(
                    headlineContent = { Text("${stringResource(R.string.fat)} [g / 100 g]") },
                    trailingContent = {
                        Text(style = MaterialTheme.typography.labelMedium,
                            text = databaseState.currentProduct.fat100g.toString())
                    }
                )
                Divider()
                ListItem(
                    headlineContent = { Text("${stringResource(R.string.carbohydrates)} [g / 100 g]") },
                    trailingContent = {
                        Text(style = MaterialTheme.typography.labelMedium,
                            text = databaseState.currentProduct.carbohydrates100g.toString())
                    }
                )
            }
        },
    )
}