package com.example.saftyapp.model

import android.net.Uri
import java.io.File

fun resolveImageModel(image: String?): Any? {
    return image?.let {
        when {
            it.startsWith("/") && File(it).exists() -> File(it)
            it.startsWith("content://") || it.startsWith("file://") -> Uri.parse(it)
            it.startsWith("http://") || it.startsWith("https://") -> it
            else -> null
        }
    }
}