package com.tele.teleflow.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.tele.teleflow.R
import kotlinx.coroutines.tasks.await
import java.util.UUID

object FirebaseUtils {
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    suspend fun uploadImage(imageUri: Uri, path: String): Result<String> {
        return try {
            val filename = "${UUID.randomUUID()}.jpg"
            val fileRef = storageRef.child("$path/$filename")

            val uploadTask = fileRef.putFile(imageUri).await()
            val downloadUrl = fileRef.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun loadImage(context: Context, url: String?, imageView: ImageView) {
        if (url.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.profile_placeholder)
        } else {
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(imageView)
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
