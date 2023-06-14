package com.chmurka.codzisnaobiad.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.R
import com.chmurka.codzisnaobiad.viewmodel.Settings
import com.chmurka.codzisnaobiad.viewmodel.SettingsViewModel
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KSuspendFunction2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    toggleDrawer: () -> Unit,
    settingsViewModel: SettingsViewModel,
    settings: Settings,
) {
    fun onDarkModeToggled(darkMode : Boolean) {
        settingsViewModel.setDarkTheme(darkMode)
    }
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { TopBar(
            scrollBehavior = scrollBehavior,
            toggleDrawer = toggleDrawer
        )},
        content = {
            Column(Modifier.padding(it)) {
                val interactionSource = remember { MutableInteractionSource() }
                Row(
                    modifier = Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            role = Role.Switch,
                            onClick = {
                                onDarkModeToggled(!settings.darkTheme)
                            }
                        )
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Text(text = stringResource(R.string.night_mode))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = settings.darkTheme,
                        onCheckedChange = {
                            onDarkModeToggled(it)
                        }
                    )
                }
            }
        }
    )
}