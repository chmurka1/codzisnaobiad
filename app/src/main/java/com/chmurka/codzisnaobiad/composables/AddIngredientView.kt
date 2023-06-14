package com.chmurka.codzisnaobiad.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.R
import com.chmurka.codzisnaobiad.database.FridgeProductEvent
import com.chmurka.codzisnaobiad.database.Product
import com.chmurka.codzisnaobiad.database.ProductState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun ProductPickCard(
    product: Product,
    onClick : () -> Unit
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
        Modifier.clickable {
            onClick()
        }
    )
}

@Composable
fun ProductPicker(
    productState: ProductState,
    onProductClick: (Product?) -> Unit
) {
    LazyColumn {
        items(productState.products) {
            ProductPickCard(product = it, onClick = { onProductClick(it) })
            Divider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AddIngredientView(
    goBack: () -> Unit,
    productState: ProductState,
    onDatabaseEvent: (FridgeProductEvent) -> Unit
) {
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val NA: String = stringResource(R.string.NA)
    var id by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf(NA) }

    var date by remember { mutableStateOf(LocalDate.now()) }
    val formatedDate by remember { derivedStateOf {
        DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date)
    } }


    val dateDialogState = rememberMaterialDialogState()

    var amount by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var amountError by remember { mutableStateOf(false) }

    var isProductPickerEnabled by remember { mutableStateOf(false) }

    if (!isProductPickerEnabled) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                MinorTopBar(
                    scrollBehavior = scrollBehavior,
                    goBack = goBack,
                    name = stringResource(R.string.add_product),
                )
            },
            content = {
                LazyColumn(
                    Modifier
                        .padding(it)
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = name)
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
                                isProductPickerEnabled = true
                            }) {
                                Text(text = stringResource(R.string.pick_product))
                            }
                        }
                    }
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = formatedDate)
                            Spacer(modifier = Modifier.weight(1f))
                            Button(onClick = {
                                dateDialogState.show()
                            }) {
                                Text(text = stringResource(R.string.pick_date))
                            }
                        }
                    }
                    item {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = amount,
                            onValueChange = { value ->
                                amountError = false
                                amount = value
                                try {
                                    onDatabaseEvent(FridgeProductEvent.SetAmount(value.text.toFloat()))
                                } catch (e: NumberFormatException) {
                                    amountError = true
                                }
                            },
                            label = { Text("${stringResource(R.string.amount)} [g]") },
                            isError = amountError
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp), horizontalArrangement = Arrangement.SpaceAround
                        )
                        {
                            Button(onClick = {
                                if (amountError || id == null ) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Invalid data"
                                        )
                                    }
                                } else {
                                    onDatabaseEvent(FridgeProductEvent.SaveFridgeProduct)
                                    goBack()
                                }
                            }) {
                                Text(
                                    stringResource(R.string.add),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            })
    } else {
        ProductPicker (
            productState = productState,
            onProductClick = { product ->
                if(product?.id != null) {
                    id = product.id
                    onDatabaseEvent(FridgeProductEvent.SetProduct(product.id))
                }
                isProductPickerEnabled = false
                if(product?.name != null)
                    name = product?.name
            }
        )
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text="OK")
            negativeButton(text=stringResource(R.string.cancel))
        }
    ) {
        datepicker(
            initialDate = LocalDate.now()
        ) {
            date = it
            onDatabaseEvent(FridgeProductEvent.SetExpirationDate(date.toString()))
        }
    }
}