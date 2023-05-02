package com.team.bpm.data.util

import android.graphics.Bitmap
import android.os.Build
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun convertBitmapToWebpFile(bitmap: Bitmap): File {
    val webpFile = File.createTempFile(
        "IMG_",
        ".webp"
    )
    val fileOutputStream = FileOutputStream(webpFile)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        bitmap.compress(
                Bitmap.CompressFormat.WEBP_LOSSY,
                50,
                fileOutputStream
            )

        fileOutputStream.close()
    }

    return webpFile
}


fun createImageMultipartBody(key: String, file: File): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name = key,
        filename = file.name,
        body = file.asRequestBody("image/*".toMediaType())
    )
}