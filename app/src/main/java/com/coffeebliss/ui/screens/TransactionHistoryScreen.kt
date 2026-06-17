package com.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.data.entity.Transaction
import com.coffeebliss.ui.components.CoffeeTopBar
import com.coffeebliss.ui.components.SectionHeader
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionHistoryScreen(
    memberId: Int,
    viewModel: CoffeeBlissViewModel,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val transactions by viewModel.memberTransactions.collectAsState()

    LaunchedEffect(memberId) {
        viewModel.refreshSelectedMember(memberId)
        viewModel.selectMember(viewModel.selectedMember.value ?: return@LaunchedEffect)
    }

    // Load transactions when member is available
    LaunchedEffect(member) {
        member?.let { viewModel.selectMember(it) }
    }

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Riwayat Transaksi",
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
        ) {
            // Summary header
            if (member != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(CoffeeBrown, CoffeeMedium)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SummaryItem(
                            label = "Total Transaksi",
                            value = transactions.filter { it.pointEarned > 0 }.size.toString(),
                            icon = Icons.Default.ShoppingCart
                        )
                        SummaryItem(
                            label = "Total Belanja",
                            value = formatCurrencyShort(transactions.filter { it.pointEarned > 0 }.sumOf { it.amount }),
                            icon = Icons.Default.AttachMoney
                        )
                        SummaryItem(
                            label = "Poin Saat Ini",
                            value = member!!.points.toString(),
                            icon = Icons.Default.Star
                        )
                    }
                }
            }

            if (transactions.isEmpty()) {
                EmptyHistory()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SectionHeader(
                            title = "Semua Transaksi",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(transactions, key = { it.id }) { transaction ->
                        TransactionCard(transaction = transaction)
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = CoffeeAccent, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = CoffeeAccentLight.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TransactionCard(transaction: Transaction) {
    val isRedeem = transaction.description.startsWith("Redeem:")
    val isNegative = transaction.pointEarned < 0

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (isRedeem) Color(0xFFE8F5E9) else CoffeeAccentLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRedeem) Icons.Default.CardGiftcard else Icons.Default.Coffee,
                    contentDescription = null,
                    tint = if (isRedeem) CoffeeGreen else CoffeeBrown,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isRedeem) transaction.description.removePrefix("Redeem: ")
                    else transaction.description.ifEmpty { "Pembelian" },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                if (!isRedeem && transaction.amount > 0) {
                    Text(
                        text = formatCurrency(transaction.amount),
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
                Text(
                    text = transaction.date,
                    fontSize = 11.sp,
                    color = Color(0xFFBBBBBB)
                )
            }

            // Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (isNegative) "" else "+"}${transaction.pointEarned}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isNegative) CoffeeRed else CoffeeGreen
                )
                Text(
                    text = "poin",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun EmptyHistory() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.History,
            contentDescription = null,
            tint = CoffeeMedium.copy(alpha = 0.4f),
            modifier = Modifier.size(70.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Belum ada transaksi",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = CoffeeMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Setiap transaksi pembelian akan muncul di sini",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}

private fun formatCurrencyShort(amount: Double): String {
    return when {
        amount >= 1_000_000 -> "Rp${String.format("%.1f", amount / 1_000_000)}jt"
        amount >= 1_000 -> "Rp${String.format("%.0f", amount / 1_000)}rb"
        else -> "Rp${amount.toInt()}"
    }
}
