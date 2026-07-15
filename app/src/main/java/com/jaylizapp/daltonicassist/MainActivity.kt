package com.jaylizapp.daltonicassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jaylizapp.daltonicassist.ui.screens.DaltonicMainScreen
import com.jaylizapp.daltonicassist.ui.theme.DaltonicAssistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaltonicAssistTheme {
                DaltonicMainScreen()
            }
        }
    }
}
