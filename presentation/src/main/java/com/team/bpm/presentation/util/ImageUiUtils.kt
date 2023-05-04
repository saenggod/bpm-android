package com.team.bpm.presentation.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

fun convertUriToBitmap(
    contentResolver: ContentResolver,
    uri: Uri,
): Bitmap {
    return ImageDecoder.decodeBitmap(
        ImageDecoder.createSource(
            contentResolver,
            uri
        )
    )
}

fun convertImageBitmapToByteArray(imageBitmap: ImageBitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    return try {
        stream.use {
            imageBitmap.asAndroidBitmap().compress(
                if (Build.VERSION.SDK_INT >= 30) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                },
                50,
                stream
            )
        }
        stream.toByteArray()
    } catch (e: Exception) {
        stream.toByteArray()
    }
}