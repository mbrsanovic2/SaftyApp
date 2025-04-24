package com.example.saftyapp.model.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.File

class PhotoViewModel : ViewModel() {
    private val _photoUris = mutableStateListOf<Uri>()
    val photoUris: List<Uri> get() = _photoUris

    var capturedPhotoUri by mutableStateOf<Uri?>(null)
        private set

    fun addPhotoUri(uri: Uri) {
        _photoUris.add(uri)
    }

    fun setCapturedPhoto(uri: Uri) {
        capturedPhotoUri = uri
    }

    fun discardCapturedPhoto() {
        capturedPhotoUri?.let { uri ->
            val file = File(uri.path ?: "")
            if (file.exists()) {
                file.delete()
            }
        }
        capturedPhotoUri = null
    }

    fun confirmCapturedPhoto() {
        capturedPhotoUri?.let { addPhotoUri(it) }
        capturedPhotoUri = null
    }

    fun analyzeCapturedPhoto(context: Context, onResult: (Boolean) -> Unit) {
        capturedPhotoUri?.let { uri ->
            val image = InputImage.fromFilePath(context, uri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Show all identified labels
                    labels.forEach { label ->
                        Log.d("MLKit", "Label: ${label.text}, Confidence: ${label.confidence}")
                    }

                    // Labels to consider as drink
                    val drinkLabels = listOf("drink", "beverage", "glass", "bottle", "cocktail", "wine", "beer", "cup", "mug", "champagne", "juice", "coffee", "tea", "liquor", "martini", "alcohol")

                    val drinkDetected = labels.any { label ->
                        drinkLabels.any { keyword ->
                            label.text.contains(keyword, ignoreCase = true)
                        }
                    }

                    if (drinkDetected) {
                        // TODO XP erhöhen und in DB speichern, check ob schon mal extra XP geholt für dieses recipe
                    }

                    onResult(drinkDetected)
                }
                .addOnFailureListener {
                    onResult(false)
                }
        }
    }
}

