package com.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.ui.components.*
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@Composable
fun AddMemberScreen(
    viewModel: CoffeeBlissViewModel,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
                ) { Text("Lihat Member") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSuccessDialog = false
                    onNavigateBack()
                }) { Text("Kembali", color = CoffeeMedium) }
            },
            title = { Text("Registrasi Berhasil! ☕", fontWeight = FontWeight.Bold) },
            text = {
                Text("Member baru atas nama $name berhasil didaftarkan.")
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Tambah Member",
                subtitle = "Registrasi Member Baru",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CoffeeSurface)
                .verticalScroll(rememberScrollState())
        ) {
            // Header illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(CoffeeBrown, CoffeeMedium)
                        )
                    )
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Bergabunglah dengan Coffee Bliss",
                        color = CoffeeAccentLight,
                        fontSize = 13.sp
                    )
                }
            }

            // Form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader(title = "Data Member")

                    Spacer(modifier = Modifier.height(4.dp))

                    CoffeeTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        label = "Nama Lengkap",
                        leadingIcon = Icons.Default.Person
                    )

                    CoffeeTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = "Email",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    CoffeeTextField(
                        value = phone,
                        onValueChange = { phone = it; errorMessage = null },
                        label = "No HP",
                        leadingIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone
                    )

                    if (errorMessage != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    CoffeeButton(
                        text = if (isLoading) "Mendaftarkan..." else "Daftar Sekarang",
                        onClick = {
                            isLoading = true
                            errorMessage = null
                            viewModel.registerMember(
                                name = name,
                                email = email,
                                phone = phone,
                                onSuccess = {
                                    isLoading = false
                                    showSuccessDialog = true
                                },
                                onError = { error ->
                                    isLoading = false
                                    errorMessage = error
                                }
                            )
                        },
                        enabled = !isLoading,
                        icon = Icons.Default.HowToReg
                    )
                }
            }

            // Info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CoffeeAccentLight)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = CoffeeBrown,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Setiap pembelian Rp10.000 = 1 Poin. Kumpulkan poin dan tukarkan dengan minuman gratis!",
                        fontSize = 12.sp,
                        color = CoffeeBrown
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
