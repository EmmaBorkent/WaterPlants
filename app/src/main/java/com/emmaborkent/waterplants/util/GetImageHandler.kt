package com.emmaborkent.waterplants.util

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class GetImageHandler(
    private val registry: ActivityResultRegistry,
    private val handleReturnedUri: (Uri) -> Unit
) : DefaultLifecycleObserver {

    private lateinit var getImage: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getImage = registry.register(
            KEY_GET_IMAGE_CODE, owner,
            ActivityResultContracts.GetContent()
        ) { uri ->
            handleReturnedUri(uri)
        }
    }

    fun selectImage() {
        getImage.launch("image/*")
    }
}