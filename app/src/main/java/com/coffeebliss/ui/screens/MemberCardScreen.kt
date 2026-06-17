package com.coffeebliss.ui.screens

import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffeebliss.data.entity.Member
import com.coffeebliss.ui.components.CoffeeTopBar
import com.coffeebliss.ui.components.SectionHeader
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun MemberCardScreen(
    memberId: Int,
    viewModel: CoffeeBlissViewModel,
    onNavigateBack: () -> Unit,
    onAddTransaction: () -> Unit,
    onRedeemReward: () -> Unit,
    onViewHistory: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()

    LaunchedEffect(memberId) {
        viewModel.refreshSelectedMember(memberId)
    }

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Kartu Member",
                subtitle = member?.name ?: "",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        if (member == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CoffeeBrown)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(CoffeeSurface)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Digital Membership Card
                MembershipCard(member = member!!)

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionHeader(title = "Aksi")
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionButton(
                            icon = Icons.Default.ShoppingCart,
                            label = "Tambah\nTransaksi",
                            onClick = onAddTransaction,
                            modifier = Modifier.weight(1f),
                            backgroundColor = CoffeeBrown
                        )
                        ActionButton(
                            icon = Icons.Default.CardGiftcard,
                            label = "Redeem\nReward",
                            onClick = onRedeemReward,
                            modifier = Modifier.weight(1f),
                            backgroundColor = CoffeeGreen
                        )
                        ActionButton(
                            icon = Icons.Default.History,
                            label = "Riwayat\nTransaksi",
                            onClick = onViewHistory,
                            modifier = Modifier.weight(1f),
                            backgroundColor = Color(0xFF5D4037)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Member Info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionHeader(title = "Informasi Member")
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoRow(icon = Icons.Default.Email, label = "Email", value = member!!.email)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEEEE))
                        InfoRow(icon = Icons.Default.Phone, label = "No HP", value = member!!.phone)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFEEEEEE))
                        InfoRow(
                            icon = Icons.Default.Badge,
                            label = "ID Member",
                            value = member!!.memberNumber.ifEmpty { "#${member!!.id}" }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun MembershipCard(member: Member) {
    val qrBitmap = remember(member.memberNumber) {
        generateQrCode("COFFEEBLISS:${member.memberNumber}:${member.email}")
    }

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .shadow(12.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C1810),
                        Color(0xFF3E2723),
                        Color(0xFF4E342E)
                    )
                )
            )
    ) {
        // Background pattern dots
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "COFFEE BLISS",
                            color = CoffeeAccent,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "MEMBER CARD",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            letterSpacing = 2.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CoffeeAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Coffee,
                            contentDescription = null,
                            tint = CoffeeBrown,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = member.name.uppercase(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ID: ${member.memberNumber.ifEmpty { "#${member.id}" }}",
                            color = CoffeeAccentLight.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        // Points display
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = member.points.toString(),
                                color = CoffeeAccent,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 36.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "POINTS",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }

                    // QR Code
                    if (qrBitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .padding(6.dp)
                        ) {
                            Image(
                                bitmap = qrBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CoffeeBrown
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = CoffeeMedium, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = TextSecondary)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
    }
}

private fun generateQrCode(content: String): Bitmap? {
    return try {
        val hints = hashMapOf<EncodeHintType, Any>(
            EncodeHintType.MARGIN to 1
        )
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) AndroidColor.BLACK else AndroidColor.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
