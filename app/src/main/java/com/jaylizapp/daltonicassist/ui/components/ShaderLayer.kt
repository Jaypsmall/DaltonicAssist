package com.jaylizapp.daltonicassist.ui.components

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import com.jaylizapp.daltonicassist.ui.shaders.ATOMIC_SHADER_SRC

@Composable
fun ShaderLayer(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    mode: Int = 1, // 0 para Onda, 1 para Atomo
    frequency: Float = 2.0f,
    canalAislar: Float = 0.0f, // 0.0 Rojo, 1.0 Verde
    content: @Composable () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && isEnabled) {
        val transition = rememberInfiniteTransition(label = "atomic")
        val time by transition.animateFloat(
            initialValue = 0f, targetValue = 1000f,
            animationSpec = infiniteRepeatable(tween(100000, easing = LinearEasing)),
            label = "time"
        )

        val shader = androidx.compose.runtime.remember { RuntimeShader(ATOMIC_SHADER_SRC) }

        Box(
            modifier = modifier
                .graphicsLayer {
                    clip = true
                    alpha = 0.99f 
                    
                    shader.setFloatUniform("u_time", time * (frequency / 6.0f))
                    shader.setFloatUniform("u_modo", mode.toFloat())
                    shader.setFloatUniform("u_canalAislar", canalAislar)
                    
                    renderEffect = RenderEffect.createRuntimeShaderEffect(shader, "u_texture").asComposeRenderEffect()
                }
        ) {
            content()
        }
    } else {
        Box(modifier = modifier) { content() }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun rememberRuntimeShader(shaderSrc: String): RuntimeShader {
    return androidx.compose.runtime.remember(shaderSrc) {
        RuntimeShader(shaderSrc)
    }
}
