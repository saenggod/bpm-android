package com.team.bpm.data.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import com.team.bpm.domain.model.Error
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun convertByteArrayToWebpFile(byteArray: ByteArray): File {
    val webpFile = File.createTempFile("IMG_", ".webp")

    try {
        FileOutputStream(webpFile).use { stream ->
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            bitmap.compress(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Bitmap.CompressFormat.WEBP_LOSSY
                } else {
                    Bitmap.CompressFormat.WEBP
                },
                50,
                stream
            )
        }
    } catch (e: Exception) {
        throw (Exception(
            Error(
                status = 0,
                error = "CONVERT_FAILED",
                code = 700,
                message = "이미지를 변환할 수 없습니다."
            )
        ))
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