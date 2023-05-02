package com.team.bpm.presentation.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

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