package com.coffeebliss.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.coffeebliss.data.entity.Member
import com.coffeebliss.ui.components.*
import com.coffeebliss.ui.theme.*
import com.coffeebliss.viewmodel.CoffeeBlissViewModel

@Composable
fun HomeScreen(
    viewModel: CoffeeBlissViewModel,
    onAddMember: () -> Unit,
    onMemberClick: (Member) -> Unit
) {
    val members by viewModel.allMembers.collectAsState()

    Scaffold(
        topBar = {
            CoffeeTopBar(
                title = "Coffee Bliss",
                subtitle = "Membership Card App"
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddMember,
                containerColor = CoffeeBrown,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) },
                text = { Text("Tambah Member", fontWeight = FontWeight.SemiBold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CoffeeSurface)
        ) {
            // Stats Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(CoffeeBrown, CoffeeMedium)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = "Total Member",
                        value = members.size.toString(),
                        icon = Icons.Default.People,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color.White
                    )
                    StatCard(
                        label = "Total Poin",
                        value = members.sumOf { it.points }.toString(),
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color.White
                    )
                }
            }

            // Members List
            if (members.isEmpty()) {
                EmptyState(onAddMember = onAddMember)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SectionHeader(
                            title = "Daftar Member",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(members, key = { it.id }) { member ->
                        MemberListCard(member = member, onClick = { onMemberClick(member) })
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
private fun MemberListCard(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CoffeeBrown, CoffeeMedium)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
                Text(
                    text = member.memberNumber.ifEmpty { "ID: #${member.id}" },
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                Text(
                    text = member.email,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Points Badge
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(CoffeeAccentLight)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = member.points.toString(),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = CoffeeBrown
                        )
                        Text(
                            text = "POIN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoffeeMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFBCAAA4),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(onAddMember: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Coffee,
            contentDescription = null,
            tint = CoffeeMedium.copy(alpha = 0.5f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Belum ada member",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = CoffeeMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Daftarkan pelanggan pertama Coffee Bliss sekarang!",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddMember,
            colors = ButtonDefaults.buttonColors(containerColor = CoffeeBrown)
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tambah Member Pertama")
        }
    }
}
