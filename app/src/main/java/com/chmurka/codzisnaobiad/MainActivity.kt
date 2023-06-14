package com.chmurka.codzisnaobiad

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chmurka.codzisnaobiad.composables.*
import com.chmurka.codzisnaobiad.database.AppDatabase
import com.chmurka.codzisnaobiad.ui.theme.AppTheme
import com.chmurka.codzisnaobiad.viewmodel.*
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private lateinit var db : AppDatabase

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(applicationContext)

        setContent {
            val settingsViewModel = ViewModelProvider(
                this,
                SettingsViewModelFactory(DataStoreRepository(dataStore))
            )[SettingsViewModel::class.java]
            val dbViewModel = ViewModelProvider(
                this,
                RecipeViewModelFactory(db.dao())
            )[RecipeViewModel::class.java]
            val dbProductsViewModel = ViewModelProvider(
                this,
                ProductViewModelFactory(db.dao())
            )[ProductViewModel::class.java]
            val dbFridgeViewModel = ViewModelProvider(
                this,
                FridgeViewModelFactory(db.dao())
            )[FridgeViewModel::class.java]
            val settings by settingsViewModel.settings.collectAsState(Settings())

            AppTheme(darkTheme = settings.darkTheme) {
                val navController = rememberNavController()

                val dbState by dbViewModel.state.collectAsState()
                val dbProductsState by dbProductsViewModel.state.collectAsState()
                val dbFridgeState by dbFridgeViewModel.state.collectAsState()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val selectedItemState : MutableState<NavItemEnum> = remember { mutableStateOf(
                    NavItemEnum.HOME) }

                fun toggleDrawer() {
                    if (drawerState.isClosed) {
                        scope.launch { drawerState.open() }
                    } else {
                        scope.launch { drawerState.close() }
                    }
                }

                fun onNavItemClick(item : NavItemEnum) {
                    navController.navigate(item.route)
                }

                fun onProductClick() {
                    navController.navigate("view_product")
                }

                fun onIngredientClick() {
                    navController.navigate("view_ingredient")
                }

                fun onAddIngredientClick() {
                    navController.navigate("add_ingredient")
                }


                fun onRecipeClick() {
                    navController.navigate("view_recipe")
                }

                fun onAddRecipeClick() {
                    navController.navigate("add_recipe")
                }

                fun onAddProductClick() {
                    navController.navigate("add_product")
                }

                fun goBackToHome() {
                    navController.navigate(NavItemEnum.HOME.route)
                }

                fun goBackToFridge() {
                    navController.navigate(NavItemEnum.FRIDGE.route)
                }

                fun goBackToProductLibrary() {
                    navController.navigate(NavItemEnum.PRODUCT_LIBRARY.route)
                }

                NavDrawer(
                    drawerState = drawerState,
                    selectedItemState = selectedItemState,
                    toggleDrawer = { scope.launch { drawerState.close() } },
                    onItemClick = { onNavItemClick(it) },
                    content = {
                        NavHost(
                            navController = navController,
                            startDestination = NavItemEnum.HOME.route
                        ) {
                            composable(NavItemEnum.HOME.route) {
                                HomeView(
                                    toggleDrawer = { toggleDrawer() },
                                    databaseState = dbState,
                                    onDatabaseEvent = dbViewModel::onEvent,
                                    onRecipeClick= ::onRecipeClick,
                                    onAddRecipeClick= ::onAddRecipeClick)
                            }
                            composable(NavItemEnum.SETTINGS.route) { SettingsView(toggleDrawer = { toggleDrawer() }, settingsViewModel = settingsViewModel, settings = settings) }
                            composable(NavItemEnum.FRIDGE.route) {
                                FridgeView(
                                    toggleDrawer = { toggleDrawer() },
                                    onAddIngredientClick = ::onAddIngredientClick,
                                    databaseState = dbFridgeState,
                                    onDatabaseEvent = dbFridgeViewModel::onEvent,
                                    onProductDatabaseEvent = dbProductsViewModel::onEvent,
                                    onIngredientClick = ::onIngredientClick
                                ) }
                            composable(NavItemEnum.PRODUCT_LIBRARY.route) {
                                ProductsListView(
                                    toggleDrawer = { toggleDrawer() },
                                    databaseState = dbProductsState,
                                    onDatabaseEvent = dbProductsViewModel::onEvent,
                                    onProductClick = ::onProductClick,
                                    onAddProductClick= ::onAddProductClick
                                ) }
                            composable(NavItemEnum.HELP.route) { HelpView(toggleDrawer = { toggleDrawer() } ) }
                            composable("add_recipe") { AddRecipeView(goBack = ::goBackToHome, onDatabaseEvent = dbViewModel::onEvent) }
                            composable("view_recipe") { RecipeView(goBack = ::goBackToHome, databaseState = dbState, onDatabaseEvent = dbViewModel::onEvent) }
                            composable("add_product") { AddProductView(goBack = ::goBackToProductLibrary, onDatabaseEvent = dbProductsViewModel::onEvent) }
                            composable("view_product") { ProductView(goBack = ::goBackToProductLibrary, databaseState = dbProductsState, onDatabaseEvent = dbProductsViewModel::onEvent) }
                            composable("add_ingredient") { AddIngredientView(goBack = ::goBackToFridge, onDatabaseEvent = dbFridgeViewModel::onEvent, productState = dbProductsState) }
                            composable("view_ingredient") { ProductView(goBack = ::goBackToFridge, databaseState = dbProductsState, onDatabaseEvent = dbProductsViewModel::onEvent) }
                        }
                    },
                )
            }
        }
    }
}