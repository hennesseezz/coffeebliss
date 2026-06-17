package com.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.data.entity.Member
import com.coffeebliss.data.repository.CoffeeBlissRepository
import com.coffeebliss.ui.components.CoffeeTopBar
import com.coffeebliss.ui.components.SectionHeader
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@Composable
fun RedeemRewardScreen(
    memberId: Int,
    viewModel: CoffeeBlissViewModel,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val rewards = viewModel.rewards
    var selectedReward by remember { mutableStateOf<CoffeeBlissRepository.Reward?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(memberId) {
        viewModel.refreshSelectedMember(memberId)
    }

    if (showConfirmDialog && selectedReward != null && member != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.redeemReward(
                            member = member!!,
                            reward = selectedReward!!,
                            onSuccess = {
                                showSuccessDialog = true
                            },
                            onError = { err ->
                                errorMessage = err
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeGreen)
                ) { Text("Konfirmasi Redeem") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Batal", color = CoffeeMedium)
                }
            },
            title = { Text("Konfirmasi Redeem", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Tukar poin dengan:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        selectedReward!!.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = CoffeeBrown
                    )
                    Text(selectedReward!!.description, fontSize = 13.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text("Poin dibutuhkan: ")
                        Text(
                            "${selectedReward!!.points} Poin",
                            fontWeight = FontWeight.Bold,
                            color = CoffeeRed
                        )
                    }
                    Row {
                        Text("Poin Anda: ")
                        Text(
                            "${member!!.points} Poin",
                            fontWeight = FontWeight.Bold,
                            color = CoffeeBrown
                        )
                    }
                    Row {
                        Text("Sisa poin: ")
                        Text(
                            "${member!!.points - selectedReward!!.points} Poin",
                            fontWeight = FontWeight.Bold,
                            color = CoffeeGreen
                        )
                    }
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showSuccessDialog && selectedReward != null) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false; onNavigateBack() },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false; onNavigateBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
                ) { Text("Kembali") }
            },
            title = { Text("Redeem Berhasil! 🎉", fontWeight = FontWeight.Bold) },
            text = {
                Text("Selamat! Kamu berhasil menukar poin dengan ${selectedReward!!.name}. Silakan tunjukkan kepada barista.")
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Redeem Reward",
                subtitle = member?.let { "${it.points} Poin Tersedia" } ?: "",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CoffeeSurface),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Points display
            if (member != null) {
                item {
                    PointsHeader(member = member!!)
                }
            }

            if (errorMessage != null) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ErrorOutline, null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            item {
                SectionHeader(title = "Pilih Reward")
            }

            items(rewards) { reward ->
                RewardCard(
                    reward = reward,
                    memberPoints = member?.points ?: 0,
                    onRedeem = {
                        errorMessage = null
                        selectedReward = reward
                        showConfirmDialog = true
                    }
                )
            }
        }
    }
}

@Composable
private fun PointsHeader(member: Member) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(CoffeeBrown, CoffeeMedium)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = CoffeeAccent,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "${member.points}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = CoffeeAccent
                )
                Text(
                    text = "Poin tersedia untuk ${member.name}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun RewardCard(
    reward: CoffeeBlissRepository.Reward,
    memberPoints: Int,
    onRedeem: () -> Unit
) {
    val canRedeem = memberPoints >= reward.points

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (canRedeem) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (canRedeem) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        if (canRedeem) CoffeeAccentLight else Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Coffee,
                    contentDescription = null,
                    tint = if (canRedeem) CoffeeBrown else Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reward.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (canRedeem) TextPrimary else Color.Gray
                )
                Text(
                    text = reward.description,
                    fontSize = 12.sp,
                    color = if (canRedeem) TextSecondary else Color.LightGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = if (canRedeem) CoffeeAccent else Color.LightGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${reward.points} Poin",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = if (canRedeem) CoffeeBrown else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (canRedeem) {
                Button(
                    onClick = onRedeem,
                    colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Redeem", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${reward.points - memberPoints}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "poin lagi",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
