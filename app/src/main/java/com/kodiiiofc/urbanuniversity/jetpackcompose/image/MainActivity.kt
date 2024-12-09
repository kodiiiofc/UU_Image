package com.kodiiiofc.urbanuniversity.jetpackcompose.image

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kodiiiofc.urbanuniversity.jetpackcompose.image.ui.theme.ImageTheme
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var expandedMenu by remember {
                mutableStateOf(false)
            }

            var face by remember {
                mutableStateOf("Лицо 1")
            }

            var hair by remember {
                mutableStateOf("Прическа 1")
            }

            var cloth by remember {
                mutableStateOf("Одежда 1")
            }

            var choosingFace by remember {
                mutableStateOf(false)
            }

            var choosingHair by remember {
                mutableStateOf(false)
            }

            var choosingCloth by remember {
                mutableStateOf(false)
            }

            ImageTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Генератор персонажа") },
                            actions = {
                                IconButton(onClick = { expandedMenu = true }) {
                                    Icon(Icons.Default.MoreVert, "Показать меню")
                                }
                                DropdownMenu(
                                    expanded = expandedMenu,
                                    onDismissRequest = { expandedMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(text = "Сброс параметров") },
                                        onClick = {
                                            face = "Лицо 1"
                                            cloth = "Одежда 1"
                                            hair = "Прическа 1"
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(text = "Выйти из приложения") },
                                        onClick = { finish() }
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        PersonImage(face, hair, cloth)

                        ExposedDropdownMenuBox(
                            expanded = choosingFace,
                            onExpandedChange = { choosingFace = it },
                        ) {
                            TextField(
                                value = face,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = choosingFace) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = choosingFace,
                                onDismissRequest = { choosingFace = false }) {
                                personGenerator.faceResourcesMap.keys.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            face = it
                                            choosingFace = false
                                        })
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = choosingHair,
                            onExpandedChange = { choosingHair = it },
                        ) {
                            TextField(
                                value = hair,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = choosingHair) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = choosingHair,
                                onDismissRequest = { choosingHair = false }) {
                                personGenerator.hairResourcesMap.keys.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            hair = it
                                            choosingHair = false
                                        })
                                }
                            }
                        }

                        ExposedDropdownMenuBox(
                            expanded = choosingCloth,
                            onExpandedChange = { choosingCloth = it },
                        ) {
                            TextField(
                                value = cloth,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = choosingCloth) },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = choosingCloth,
                                onDismissRequest = { choosingCloth = false }) {
                                personGenerator.clothResourcesMap.keys.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            cloth = it
                                            choosingCloth = false
                                        })
                                }
                            }
                        }

                    }


                }
            }
        }
    }
}

val personGenerator = PersonGeneratorClass()

@Composable
fun PersonImage(face: String, hair: String, cloth: String) {
    Box(
        Modifier
            .size(200.dp, 300.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Image(
            painterResource(personGenerator.faceResourcesMap[face]!!),
            "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painterResource(personGenerator.clothResourcesMap[cloth]!!),
            "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 1f }
        )

        Image(
            painterResource(personGenerator.hairResourcesMap[hair]!!),
            "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}