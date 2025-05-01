package com.example.saftyapp.presentation.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberImagePainter
import com.example.saftyapp.model.viewmodels.PhotoViewModel
import java.io.File

@Composable
fun CameraScreen(
    viewModel: PhotoViewModel,
    onPhotoTaken: () -> Unit,
    onDrinkDetected: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val outputDirectory = remember { getOutputDirectory(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var capturedPhotoUri = viewModel.capturedPhotoUri

    if (capturedPhotoUri == null) {
        // Camera view
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(factory = { ctx ->
                // Request CameraProvider to bind camera lifecycle to LifecycleOwner
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                // Select a camera and bind the lifecycle and use cases
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Preview
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // Select back camera as a default
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    // Bind use cases to camera
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, preview, imageCapture
                    )

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }, modifier = Modifier.fillMaxSize())

            // Capture Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
            ) {
                CameraCaptureButton {
                    takePhoto(context, imageCapture, outputDirectory) { uri ->
                        viewModel.setCapturedPhoto(uri)
                    }
                }
            }
        }
    } else {
        // Photo preview & Option to keep or make new photo
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberImagePainter(capturedPhotoUri),
                contentDescription = "Captured Photo",
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    // Analyze Picture if drink is visible
                    viewModel.analyzeCapturedPhoto(context) { drinkDetected ->
                        if (drinkDetected) {
                            Toast.makeText(context, "Drink detected! +10 XP 🎉", Toast.LENGTH_SHORT)
                                .show()
                            onDrinkDetected()
                        } else {
                            Toast.makeText(
                                context,
                                "No drink detected. Take another photo for extra XP.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        // Keep photo and go back to instruction screen
                        viewModel.confirmCapturedPhoto()
                        onPhotoTaken()
                    }
                }) {
                    Text("Save")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    // Delete photo and resume camera function
                    viewModel.discardCapturedPhoto()
                }) {
                    Text("New photo")
                }
            }
        }
    }
}

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    outputDirectory: File,
    onPhotoTaken: (Uri) -> Unit
) {
    // Create time stamped name and MediaStore entry
    val photoFile = File(
        outputDirectory,
        "drink_${System.currentTimeMillis()}.jpg"
    )

    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // Set up image capture listener, which is triggered after photo has been taken
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onPhotoTaken(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Photo capture failed: ${exception.message}", exception)
            }
        }
    )
}

fun getOutputDirectory(context: Context): File {
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, "camera_photos").apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else context.filesDir
}

@Composable
fun CameraCaptureButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // outer circle
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
        )
        // inner circle
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .clickable { onClick() }
        )
    }
}

