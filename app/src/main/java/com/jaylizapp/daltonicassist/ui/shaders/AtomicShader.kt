package com.jaylizapp.daltonicassist.ui.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
const val ATOMIC_SHADER_SRC = """
    uniform shader u_texture;
    uniform half u_time;
    uniform half u_modo;
    uniform half u_canalAislar; // 0.0 para Rojo, 1.0 para Verde

    float ruidoAtomico(float2 co) {
        return fract(sin(dot(co.xy, float2(12.9898, 58.233))) * 23758.5453);
    }

    half4 main(float2 fragCoord) {
        half4 texColor = u_texture.eval(fragCoord);
        half3 color = texColor.rgb;

        // --- MÁSCARAS BRANCHLESS (Optimización GPU) ---
        // Máscara Rojo: R dominante sobre G y B
        half maxG_B = max(color.g, color.b);
        half mascaraRojo = step(0.3, color.r - maxG_B) * step(0.3, color.r);

        // Máscara Verde: G dominante sobre R y B
        half maxR_B = max(color.r, color.b);
        half mascaraVerde = step(0.25, color.g - maxR_B) * step(0.25, color.g);

        // Selección de canal a aislar (0 = Rojo, 1 = Verde)
        half mascaraFinal = mix(mascaraRojo, mascaraVerde, step(0.5, u_canalAislar));

        // --- EFECTOS ---
        half onda = step(0.0, sin(fragCoord.y * 0.1 + u_time * 5.0));
        half atomo = step(0.09, ruidoAtomico(fragCoord.xy + float2(u_time * 6.0, u_time * 2.5)));
        
        // Selección de modo de efecto
        half efectoElegido = mix(onda, atomo, step(0.5, u_modo));

        // --- CÁLCULO DE COLOR ASISTIDO ---
        half brillo = (color.r + color.g + color.b) * 0.33333;
        half esNaranja = smoothstep(0.18, 0.3, color.g);

        half3 azulElectrico = half3(0.0, 0.3 * brillo, 2.5 * brillo);
        half3 celesteAtomico = half3(0.0, 1.8 * brillo, 3.5 * brillo);
        half3 colorEfecto = mix(azulElectrico, celesteAtomico, esNaranja);

        // Aplicación final del efecto
        half aplicarAhora = mascaraFinal * efectoElegido;
        return half4(mix(color, colorEfecto, aplicarAhora), texColor.a);
    }
"""
