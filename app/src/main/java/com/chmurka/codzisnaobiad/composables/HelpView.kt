package com.chmurka.codzisnaobiad.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpView(
    toggleDrawer: () -> Unit
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(
            scrollBehavior = scrollBehavior,
            toggleDrawer = toggleDrawer
        )},
        content = {
            Column(Modifier.padding(it)) {
                Text(text = "pomoc")
            }
        }
    )
}