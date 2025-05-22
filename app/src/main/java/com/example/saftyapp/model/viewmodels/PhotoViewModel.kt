package com.example.saftyapp.model.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saftyapp.model.database.Repository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    var capturedPhotoUri by mutableStateOf<Uri?>(null)
        private set

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

    suspend fun savePhotoInDB(recipeName: String) {
        val path =
            capturedPhotoUri?.path ?: throw IllegalStateException("confirmedPhotoUri is null")

        repository.RecipeFunctions().photoScoreRecipe(recipeName)
        repository.ArchiveFunctions().addCustomPhoto(path, recipeName)
        capturedPhotoUri = null
    }

    fun analyzeCapturedPhoto(context: Context, onResult: (Boolean) -> Unit) {
        capturedPhotoUri?.let { uri ->
            val image = InputImage.fromFilePath(context, uri)
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Show all identified labels
//                    labels.forEach { label ->
//                        Log.d("MLKit", "Label: ${label.text}, Confidence: ${label.confidence}")
//                    }

                    // Labels to consider as drink
                    val drinkLabels = listOf(
                        "drink",
                        "beverage",
                        "glass",
                        "bottle",
                        "cocktail",
                        "wine",
                        "beer",
                        "cup",
                        "mug",
                        "champagne",
                        "juice",
                        "coffee",
                        "tea",
                        "liquor",
                        "martini",
                        "alcohol"
                    )

                    val drinkDetected = labels.any { label ->
                        drinkLabels.any { keyword ->
                            label.text.contains(keyword, ignoreCase = true)
                        }
                    }

                    if (drinkDetected) {
                        Toast.makeText(context, "Drink detected! +10 XP 🎉", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            context,
                            "No drink detected. Take another photo for extra XP.",
                            Toast.LENGTH_SHORT
                        ).show()
                        discardCapturedPhoto()
                    }

                    onResult(drinkDetected)
                }
                .addOnFailureListener {
                    onResult(false)
                }
        }
    }
}

