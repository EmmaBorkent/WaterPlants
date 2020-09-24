package com.emmaborkent.waterplants.util

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class GetImagePermissionHandler(
    private val registry: ActivityResultRegistry,
    private val handlePermissionResult: (Boolean) -> Unit
) : DefaultLifecycleObserver {

    private lateinit var getPermission: ActivityResultLauncher<Boolean>

    override fun onCreate(owner: LifecycleOwner) {


    }
}