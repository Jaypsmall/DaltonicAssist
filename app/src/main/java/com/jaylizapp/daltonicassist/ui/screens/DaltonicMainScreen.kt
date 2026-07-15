package com.jaylizapp.daltonicassist.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.jaylizapp.daltonicassist.R
import com.jaylizapp.daltonicassist.ui.components.CameraPreview
import com.jaylizapp.daltonicassist.ui.components.ShaderLayer
import com.jaylizapp.daltonicassist.ui.theme.*
import kotlinx.coroutines.launch

enum class DaltonicProfile(
    val canal: Float,
    val displayName: String,
    val icon: ImageVector,
    val detail: String
) {
    PROTANOPIA(0.0f, "Protanopia", Icons.Default.Visibility, "Asistencia para Rojos"),
    DEUTERANOPIA(1.0f, "Deuteranopia", Icons.Default.Visibility, "Asistencia para Verdes"),
}

@Composable
fun DaltonicMainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // --- ESTADO GLOBAL ---
    var activeProfile by remember { mutableStateOf(DaltonicProfile.PROTANOPIA) }
    var canalAislar by remember { mutableFloatStateOf(activeProfile.canal) }
    var frequency by remember { mutableFloatStateOf(6.0f) }
    var zoomRatio by remember { mutableFloatStateOf(1.0f) }
    var isEnabled by remember { mutableStateOf(true) }
    var activeEffectMode by remember { mutableIntStateOf(1) } // 0: Onda, 1: Atomo

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = TechDark,
                modifier = Modifier.width(300.dp),
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                // --- CABECERA PREMIUM ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(TechBlue.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .border(2.dp, TechBlue, CircleShape)
                                .padding(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.daltonic_assist_icon),
                                contentDescription = "Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("DALTONIC", color = TechBlue, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, letterSpacing = 1.sp)
                        Text("ASSIST PRO", color = Color.White, fontWeight = FontWeight.Light, fontSize = 20.sp, letterSpacing = 4.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECCIONES DEL MENÚ ---
                MenuLabel("MODOS DE VISIÓN")
                DaltonicProfile.entries.forEach { profile ->
                    NavigationDrawerItem(
                        label = { 
                            Column {
                                Text(profile.displayName, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(profile.detail, color = Color.Gray, fontSize = 10.sp)
                            }
                        },
                        selected = activeProfile == profile,
                        onClick = {
                            activeProfile = profile
                            canalAislar = profile.canal
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(profile.icon, null, tint = if (activeProfile == profile) TechBlue else Color.Gray) },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = TechBlue.copy(alpha = 0.15f)
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                MenuLabel("SISTEMA")
                DrawerItemCustom(Icons.Default.Settings, "Ajustes de Interfaz") {}
                DrawerItemCustom(Icons.AutoMirrored.Filled.HelpOutline, "Centro de Ayuda") {}
                DrawerItemCustom(Icons.Default.Share, "Compartir App") {}

                Spacer(modifier = Modifier.weight(1f))

                // --- FIRMA FINAL PROFESIONAL ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(color = TechBlue.copy(alpha = 0.2f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "DaltonicAssist v1.0.1",
                        color = TechBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Created by JAYLIZ with ",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "❤️",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(TechDark)) {
            if (hasCameraPermission) {
                ShaderLayer(
                    modifier = Modifier.fillMaxSize(),
                    isEnabled = isEnabled,
                    mode = activeEffectMode,
                    frequency = frequency,
                    canalAislar = canalAislar
                ) {
                    CameraPreview(onZoomChange = { zoomRatio = it })
                }
            }

            // --- BARRA SUPERIOR PROFESIONAL ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, null, tint = TechCyan, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DALTONIC ASSIST",
                            color = TechCyan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            // --- BOTÓN MASTER FLOTANTE ---
            FloatingActionButton(
                onClick = { isEnabled = !isEnabled },
                containerColor = if (isEnabled) TechCyan else Color.DarkGray,
                contentColor = TechDark,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 24.dp)
                    .size(60.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(
                    imageVector = if (isEnabled) Icons.Default.PowerSettingsNew else Icons.Default.PowerOff,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            // --- PANEL DE CONTROL INFERIOR ---
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 20.dp, bottom = 40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .width(280.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    HudSectionSmall(title = "MODO EFECTO") {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ModeIconSmall("〰️", activeEffectMode == 0) { activeEffectMode = 0 }
                            ModeIconSmall("░", activeEffectMode == 1) { activeEffectMode = 1 }
                        }
                    }

                    HudSectionSmall(title = "AISLAR CANAL") {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            LetterCircle("R", TechRed, canalAislar == 0.0f) { canalAislar = 0.0f }
                            LetterCircle("G", TechGreen, canalAislar == 1.0f) { canalAislar = 1.0f }
                        }
                    }
                }

                HudSectionSmall(title = "FRECUENCIA CUÁNTICA") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Slider(
                            value = frequency,
                            onValueChange = { frequency = it },
                            valueRange = 0.5f..12f,
                            colors = SliderDefaults.colors(thumbColor = TechCyan, activeTrackColor = TechCyan)
                        )
                        Text(
                            text = String.format("%.1f Hz", frequency),
                            color = TechCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- INDICADOR DE ZOOM ---
            if (zoomRatio > 1.05f) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 240.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = String.format("%.1f x", zoomRatio),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MenuLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 24.dp, top = 16.dp, bottom = 8.dp),
        color = TechBlue.copy(alpha = 0.6f),
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 2.sp
    )
}

@Composable
fun DrawerItemCustom(icon: ImageVector, label: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, color = Color.White, fontSize = 14.sp) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, null, tint = Color.Gray) },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Composable
fun ModeIconSmall(icon: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(if (selected) TechCyan.copy(alpha = 0.35f) else Color.Black.copy(alpha = 0.6f), CircleShape)
            .border(1.5.dp, if (selected) TechCyan else Color.White.copy(alpha = 0.2f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(icon, color = if (selected) TechCyan else Color.White, fontSize = 18.sp)
    }
}

@Composable
fun LetterCircle(letter: String, color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(color)
            .border(if (isSelected) 3.dp else 0.dp, Color.White, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter, color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
    }
}

@Composable
fun HudSectionSmall(title: String, content: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(title, color = TechCyan.copy(alpha = 0.9f), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
