package com.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.ui.components.*
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddTransactionScreen(
    memberId: Int,
    viewModel: CoffeeBlissViewModel,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successPoints by remember { mutableStateOf<Int?>(null) }

    val amount = amountText.toLongOrNull() ?: 0L
    val estimatedPoints = (amount / 10000).toInt()

    LaunchedEffect(memberId) {
        viewModel.refreshSelectedMember(memberId)
    }

    if (successPoints != null) {
        AlertDialog(
            onDismissRequest = { successPoints = null; onNavigateBack() },
            confirmButton = {
                Button(
                    onClick = { successPoints = null; onNavigateBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
                ) { Text("Kembali ke Kartu") }
            },
            title = { Text("Transaksi Berhasil! ☕", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Transaksi sebesar:")
                    Text(
                        text = formatCurrency(amount),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CoffeeBrown
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Poin didapat:")
                    Text(
                        text = "+$successPoints Poin",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = CoffeeGreen
                    )
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Tambah Transaksi",
                subtitle = member?.name ?: "",
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Member info
            if (member != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoffeeBrown)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = CoffeeAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(member!!.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                "Poin saat ini: ${member!!.points}",
                                color = CoffeeAccentLight.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Transaction form
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SectionHeader(title = "Detail Transaksi")

                    // Amount input
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = {
                            if (it.all { c -> c.isDigit() }) {
                                amountText = it
                                errorMessage = null
                            }
                        },
                        label = { Text("Nominal Pembelian (Rp)") },
                        leadingIcon = {
                            Icon(Icons.Default.AttachMoney, contentDescription = null, tint = CoffeeMedium)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CoffeeBrown,
                            focusedLabelColor = CoffeeBrown
                        ),
                        singleLine = true,
                        suffix = {
                            if (amountText.isNotEmpty()) {
                                Text(
                                    formatCurrency(amount),
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Keterangan (opsional)") },
                        leadingIcon = {
                            Icon(Icons.Default.Notes, contentDescription = null, tint = CoffeeMedium)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CoffeeBrown,
                            focusedLabelColor = CoffeeBrown
                        ),
                        singleLine = true
                    )

                    // Point estimation
                    if (amount > 0) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = CoffeeAccentLight)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        "Estimasi Poin:",
                                        fontSize = 12.sp,
                                        color = CoffeeMedium
                                    )
                                    Text(
                                        text = "$amount ÷ 10.000 = $estimatedPoints Poin",
                                        fontSize = 13.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = "+$estimatedPoints",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp,
                                    color = CoffeeBrown
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                    }
                }
            }

            CoffeeButton(
                text = if (isLoading) "Menyimpan..." else "Simpan Transaksi",
                onClick = {
                    isLoading = true
                    errorMessage = null
                    viewModel.addTransaction(
                        memberId = memberId,
                        amount = amount.toDouble(),
                        description = description,
                        onSuccess = { points ->
                            isLoading = false
                            successPoints = points
                        },
                        onError = { err ->
                            isLoading = false
                            errorMessage = err
                        }
                    )
                },
                enabled = !isLoading && amount > 0,
                icon = Icons.Default.Save
            )
        }
    }
}

private fun formatCurrency(amount: Long): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}
