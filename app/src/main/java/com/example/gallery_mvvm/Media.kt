package com.example.gallery_mvvm

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Media(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
    val latitude: String,
    val longitude: String
)

// Run the querying logic in a coroutine outside of the main thread to keep the app responsive.
// Keep in mind that this code snippet is querying only images of the shared storage.
