package com.chmurka.codzisnaobiad.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.AbsoluteAlignment.TopRight
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.R
import java.util.*

enum class NavItemEnum (val icon : ImageVector, val descId: Int, val route : String) {
    HOME(Icons.Filled.Home, R.string.home, "home"),
    FRIDGE(Icons.Filled.AcUnit, R.string.fridge, "fridge"),
    PRODUCT_LIBRARY(Icons.Outlined.Category, R.string.product_library, "product_library"),
    SETTINGS(Icons.Filled.Settings, R.string.settings, "settings"),
    HELP(Icons.Filled.Info, R.string.help, "help")
}

@Composable
fun NavDrawer (
    modifier : Modifier = Modifier,
    content : @Composable (()-> Unit),
    drawerState : DrawerState,
    items : Array<NavItemEnum> = NavItemEnum.values(),
    onItemClick : (NavItemEnum) -> Unit = {},
    selectedItemState : MutableState<NavItemEnum>,
    toggleDrawer: () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = TopRight
                ) {
                    IconButton(
                        onClick = { toggleDrawer() },
                        content = {
                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    )
                }
                Spacer(modifier.height(12.dp))
                items.forEach { item ->
                    val desc : String = stringResource(item.descId)
                    NavigationDrawerItem(
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = { Text(desc) },
                        selected = item == selectedItemState.value,
                        onClick = {
                            toggleDrawer()
                            selectedItemState.value = item
                            onItemClick(item)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    )
                }
            }
        },
        content = { content() },
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    toggleDrawer : () -> Unit,
    scrollBehavior : TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = { toggleDrawer() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu drawer"
                ) }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinorTopBar(
    goBack : () -> Unit,
    scrollBehavior : TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    name : String
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        title = {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = { goBack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                ) }
        },
        scrollBehavior = scrollBehavior,
    )
}