package com.example.smartcook.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Rive
import com.example.smartcook.R

@Composable
fun RiveLoadingAnimation(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    AndroidView(
        factory = {
            Rive.init(context)
            RiveAnimationView(context).apply {
                setRiveResource(R.raw.loading_animation)
                play()
            }
        },
        modifier = modifier
    )
}