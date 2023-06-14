package com.chmurka.codzisnaobiad.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.core.content.ContextCompat
import java.io.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.Q)
fun saveFile(uri: Uri, context: Context) : String? {
    var savedPath: String? = null
    var outputStream: OutputStream? = null
    var inputStream: InputStream? = null
    try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
        val filename = LocalDateTime.now().format(formatter)
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/codzisnaobiad")
            }
        }
        val outUri: Uri? = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        if(outUri != null) {
            outputStream = context.contentResolver.openOutputStream(outUri)
            inputStream = context.contentResolver.openInputStream(uri)

            val buff = ByteArray(1024)
            inputStream?.read(buff)
            do {
                outputStream?.write(buff)
            } while (inputStream?.read(buff) != -1)
        }

        savedPath = "${Environment.getExternalStorageDirectory().path}/Pictures/codzisnaobiad/${filename}.jpg"
    } catch (e: IOException) {
        Toast.makeText(context, "Fail to create file", Toast.LENGTH_SHORT).show()
    } finally {
        outputStream?.close()
        inputStream?.close()
    }
    return savedPath
}

fun takePhoto(
    context: Context,
    cameraController: CameraController,
    onPhotoTaken: (ImageCapture.OutputFileResults, String) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS")
    val filename = LocalDateTime.now().format(formatter)
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.MediaColumns.RELATIVE_PATH,"Pictures/codzisnaobiad")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues)
        .build()

    val path = "${Environment.getExternalStorageDirectory().path}/Pictures/codzisnaobiad/${filename}.jpg"

    cameraController.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object: ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Toast.makeText(context, "An error occured while taking a photo", Toast.LENGTH_LONG).show()
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                onPhotoTaken(output, path)
            }
        }
    )
}