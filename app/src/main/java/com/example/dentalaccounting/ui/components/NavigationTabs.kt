package com.example.dentalaccounting.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TabItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

val NAV_TABS = listOf(
    TabItem("dashboard", "الرئيسية", Icons.Default.Dashboard),
    TabItem("daily-income", "الدخل اليومي", Icons.Default.Payments),
    TabItem("labs", "المعامل", Icons.Default.Biotech),
    TabItem("supplies", "المستلزمات", Icons.Default.Inventory2),
    TabItem("expenses", "الخرجيات", Icons.Default.ReceiptLong),
    TabItem("debts", "أقساط المرضى", Icons.Default.PersonSearch),
    TabItem("personal-accounts", "الحسابات الشخصية", Icons.Default.AccountBalanceWallet),
    TabItem("general-audit", "الجرد العام", Icons.Default.Assessment)
)

@Composable
fun NavigationTabs(
    activeTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(NAV_TABS) { tab ->
                val isSelected = activeTab == tab.id
                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .clickable { onTabSelected(tab.id) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("nav_tab_${tab.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = contentColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tab.title,
                            color = contentColor,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
