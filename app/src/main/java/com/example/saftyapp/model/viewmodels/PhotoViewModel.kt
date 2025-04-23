package com.example.saftyapp.model.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
}

