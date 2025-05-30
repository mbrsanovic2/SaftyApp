package com.example.saftyapp.model

import android.net.Uri
import java.io.File

fun resolveImageModel(image: String?): Any? {
    return image?.let {
        when {
            it.startsWith("/") && File(it).exists() -> File(it) // local file path
            it.startsWith("content://") || it.startsWith("file://") -> Uri.parse(it) // URI
            it.startsWith("http://") || it.startsWith("https://") -> it // web URL as String
            else -> null
        }
    }
}