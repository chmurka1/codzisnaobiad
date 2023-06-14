package com.chmurka.codzisnaobiad.composables

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.chmurka.codzisnaobiad.R
import com.chmurka.codzisnaobiad.database.ProductEvent
import com.chmurka.codzisnaobiad.offutils.openFoodFactsApiCall
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch

fun scannerView(
    onBarcodeScanned: (String?) -> Unit,
    context: Context
) {
    val scanner = GmsBarcodeScanning.getClient(context)

    scanner.startScan()
        .addOnSuccessListener { barcode -> onBarcodeScanned(barcode.rawValue) }
        .addOnCanceledListener {}
        .addOnFailureListener { Toast.makeText(context, "Error occured while scanning codebar", Toast.LENGTH_LONG).show() }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AddProductView(
    goBack: () -> Unit,
    onDatabaseEvent: (ProductEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var proteins by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("0.0"))
    }

    var fat by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("0.0"))
    }

    var carbs by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("0.0"))
    }

    var energy by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("0"))
    }

    var proteinsError by remember { mutableStateOf(false) }
    var fatError by remember { mutableStateOf(false) }
    var carbsError by remember { mutableStateOf(false) }
    var energyError by remember { mutableStateOf(false) }

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
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        FilledIconButton(
                            modifier = Modifier.size(80.dp),
                            onClick = {
                                scannerView(
                                    onBarcodeScanned = { barcode -> run {
                                        if (barcode != null) {
                                            openFoodFactsApiCall(
                                                barcode,
                                                { res ->
                                                    if(res.status != 0) {
                                                        name = TextFieldValue(res.product?.product_name?:"")
                                                        proteins = TextFieldValue("${res.product?.nutriments?.proteins_100g}")
                                                        fat = TextFieldValue("${res.product?.nutriments?.fat_100g}")
                                                        carbs = TextFieldValue("${res.product?.nutriments?.carbohydrates_100g}")
                                                        energy = TextFieldValue("${res.product?.nutriments?.energy_kcal_100g}")
                                                        onDatabaseEvent(ProductEvent.SetName(res.product?.product_name?:""))
                                                        onDatabaseEvent(ProductEvent.SetProteins(res.product?.nutriments?.proteins_100g?:0f))
                                                        onDatabaseEvent(ProductEvent.SetFat(res.product?.nutriments?.fat_100g?:0f))
                                                        onDatabaseEvent(ProductEvent.SetCarbs(res.product?.nutriments?.carbohydrates_100g?:0f))
                                                        onDatabaseEvent(ProductEvent.SetKcal(res.product?.nutriments?.energy_kcal_100g?:0))
                                                    } else {
                                                        scope.launch {
                                                            snackbarHostState.showSnackbar(
                                                                "Invalid barcode"
                                                            )
                                                        }
                                                    }
                                                }, { scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        "Invalid barcode"
                                                    )
                                                } }, context
                                            )
                                        }
                                    }}, context
                                )
                            }) {
                            Icon(
                                painterResource(R.drawable.barcode_scanner),
                                contentDescription = null,
                                Modifier.size(48.dp)
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = name,
                        onValueChange = { value ->
                            onDatabaseEvent(ProductEvent.SetName(value.text))
                            name = value
                        },
                        label = { Text(stringResource(R.string.name)) },
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = energy,
                        onValueChange = { value ->
                            energyError = false
                            energy = value
                            try {
                                onDatabaseEvent(ProductEvent.SetKcal(value.text.toInt()))
                            } catch (e: NumberFormatException) {
                                energyError = true
                            }
                        },
                        label = { Text("${stringResource(R.string.energy)} [kcal / 100 g]") },
                        isError = energyError
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = proteins,
                        onValueChange = { value ->
                            proteinsError = false
                            proteins = value
                            try {
                                onDatabaseEvent(ProductEvent.SetProteins(value.text.toFloat()))
                            } catch (e: NumberFormatException) {
                                proteinsError = true
                            }
                        },
                        label = { Text("${stringResource(R.string.proteins)} [g / 100 g]") },
                        isError = proteinsError
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = fat,
                        onValueChange = { value ->
                            fatError = false
                            fat = value
                            try {
                                onDatabaseEvent(ProductEvent.SetFat(value.text.toFloat()))
                            } catch (e: NumberFormatException) {
                                fatError = true
                            }
                        },
                        label = { Text("${stringResource(R.string.fat)} [g / 100 g]") },
                        isError = fatError
                    )
                }
                item {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = carbs,
                        onValueChange = { value ->
                            carbsError = false
                            carbs = value
                            try {
                                onDatabaseEvent(ProductEvent.SetCarbs(value.text.toFloat()))
                            } catch (e: NumberFormatException) {
                                carbsError = true
                            }
                        },
                        label = { Text("${stringResource(R.string.carbohydrates)} [g / 100 g]") },
                        isError = carbsError
                    )
                }

                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.SpaceAround)
                    {
                        Button(onClick = {
                            if(proteinsError || fatError || carbsError || energyError || name.text.isBlank()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Invalid data"
                                    )
                                }
                            } else {
                                onDatabaseEvent(ProductEvent.SaveProduct)
                                goBack()
                            }
                        }) {
                            Text(stringResource(R.string.add), style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        })
}