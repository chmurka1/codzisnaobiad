package com.chmurka.codzisnaobiad.composables

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.chmurka.codzisnaobiad.R
import com.chmurka.codzisnaobiad.database.RecipeEvent
import com.chmurka.codzisnaobiad.util.saveFile
import com.chmurka.codzisnaobiad.util.takePhoto
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun CameraPreview(
    onGoBack: () -> Unit,
    onPhotoTaken: (ImageCapture.OutputFileResults, String) -> Unit,
    onOpenGallery: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val previewView = PreviewView(context)
    val cameraController = LifecycleCameraController(context)
    cameraController.bindToLifecycle(lifecycleOwner)
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    previewView.controller = cameraController

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize(),
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FilledTonalIconButton(
                modifier = Modifier.size(60.dp),
                onClick = onGoBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null, Modifier.size(36.dp))
            }
            FilledIconButton(
                modifier = Modifier.size(80.dp),
                onClick = { takePhoto(context, cameraController, onPhotoTaken) } ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = null, Modifier.size(48.dp))
            }
            FilledTonalIconButton(
                modifier = Modifier.size(60.dp),
                onClick = onOpenGallery) {
                Icon(Icons.Filled.PhotoLibrary, contentDescription = null, Modifier.size(36.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddRecipeView(
    goBack: () -> Unit,
    onDatabaseEvent: (RecipeEvent) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    var isCameraEnabled by remember {
        mutableStateOf(false)
    }

    fun saveUri(uri: Uri?, path: String?) {
        imageUri = uri
        onDatabaseEvent(RecipeEvent.SetUri(uri = path))
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            run {
                if(uri != null) {
                    saveUri(uri, saveFile(uri, context))
                    isCameraEnabled = false
                }
            }
        }
    )

    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var desc by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    if(!isCameraEnabled) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MinorTopBar(
                    scrollBehavior = scrollBehavior,
                    goBack = goBack,
                    name = stringResource(R.string.add_recipe),
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
                                FilledTonalButton(onClick = {
                                    when {
                                        !permissionState.status.isGranted -> {
                                            permissionState.launchPermissionRequest()
                                        }
                                    }
                                    if(permissionState.status.isGranted) isCameraEnabled = true
                                }) {
                                    Text(stringResource(R.string.add_photo))
                                }
                            }
                        }
                        item {
                            if (imageUri != null) {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Icon(
                                        Icons.Filled.NoPhotography,
                                        contentDescription = null,
                                        Modifier.size(200.dp)
                                    )
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = name,
                                onValueChange = { value ->
                                    onDatabaseEvent(RecipeEvent.SetName(value.text))
                                    name = value
                                },
                                label = { Text(stringResource(R.string.name)) },
                            )
                        }
                        item {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = desc,
                                onValueChange = { value ->
                                    onDatabaseEvent(RecipeEvent.SetDesc(value.text))
                                    desc = value
                                },
                                label = { Text(stringResource(R.string.description)) },
                            )
                        }

                        item {
                            FilledTonalButton(onClick = {
                                onDatabaseEvent(RecipeEvent.SaveRecipe)
                                goBack()
                            }) {
                                Text(stringResource(R.string.add))
                            }
                        }
                    }
                })
    } else {
        CameraPreview(onGoBack = { isCameraEnabled = false },
            onPhotoTaken = { output, path ->
                run {
                    saveUri(output.savedUri, path)
                    isCameraEnabled = false
                }
            },
            onOpenGallery = { imagePickerLauncher.launch(PickVisualMediaRequest(ImageOnly)) })
    }
}