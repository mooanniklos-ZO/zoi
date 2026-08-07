package com.example.dentalaccounting.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.example.dentalaccounting.data.*
import java.util.Locale

@Composable
fun DashboardView(
    dailyIncome: List<DailyIncomeEntity>,
    dentalLabs: List<DentalLabEntity>,
    dentalSupplies: List<DentalSupplierEntity>,
    expenses: List<ExpenseEntity>,
    patientDebts: List<PatientDebtEntity>,
    personalAccounts: List<PersonalAccountEntity>,
    onNavigateTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCashReceived = dailyIncome.sumOf { it.paidAmount }
    val totalAgreedAmount = dailyIncome.sumOf { it.totalAmount }
    val totalLabPaid = dentalLabs.sumOf { it.paidAmount }
    val totalLabRemaining = dentalLabs.sumOf { it.remainingAmount }
    val totalSupplierPaid = dentalSupplies.sumOf { it.paidAmount }
    val totalSupplierRemaining = dentalSupplies.sumOf { it.remainingAmount }
    val totalExpenses = expenses.sumOf { it.totalExpense }
    val totalPatientDebts = patientDebts.sumOf { it.remainingAmount }
    val netProfit = totalCashReceived - (totalLabPaid + totalSupplierPaid + totalExpenses)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Net Profit Card
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (netProfit >= 0) Color(0xFF0F4C81) else Color(0xFFC62828)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "صافي النقدية الحالية بالخزينة",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "العيادة الحالية",
                                color = Color.White,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${formatMoney(netProfit)} ريال يمني",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "إجمالي المقبوضات",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${formatMoney(totalCashReceived)} ريال",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column {
                            Text(
                                text = "إجمالي المصاريف والخرجيات",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${formatMoney(totalLabPaid + totalSupplierPaid + totalExpenses)} ريال",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Summary Metric Cards (2x2 Grid / Row)
        item {
            Text(
                text = "المؤشرات المالية الرئيسية",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "الدخل اليومي (المقبوض)",
                        value = "${formatMoney(totalCashReceived)} ريال",
                        subtext = "إجمالي الاتفاق: ${formatMoney(totalAgreedAmount)} ريال",
                        icon = Icons.Default.Payments,
                        iconBg = Color(0xFFE8F5E9),
                        iconColor = Color(0xFF2E7D32),
                        onClick = { onNavigateTab("daily-income") },
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "مصاريف المعامل",
                        value = "${formatMoney(totalLabPaid)} ريال",
                        subtext = "متبقي للمعامل: ${formatMoney(totalLabRemaining)} ريال",
                        icon = Icons.Default.Biotech,
                        iconBg = Color(0xFFE3F2FD),
                        iconColor = Color(0xFF1565C0),
                        onClick = { onNavigateTab("labs") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "مستلزمات الأسنان",
                        value = "${formatMoney(totalSupplierPaid)} ريال",
                        subtext = "متبقي للموردين: ${formatMoney(totalSupplierRemaining)} ريال",
                        icon = Icons.Default.Inventory2,
                        iconBg = Color(0xFFFFF3E0),
                        iconColor = Color(0xFFE65100),
                        onClick = { onNavigateTab("supplies") },
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "خرجيات العيادة",
                        value = "${formatMoney(totalExpenses)} ريال",
                        subtext = "ممرضة + مطبخ + وقود + نثرية",
                        icon = Icons.Default.ReceiptLong,
                        iconBg = Color(0xFFFFEBEE),
                        iconColor = Color(0xFFC62828),
                        onClick = { onNavigateTab("expenses") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "ديون المرضى المستحقة",
                        value = "${formatMoney(totalPatientDebts)} ريال",
                        subtext = "الذين عليهم أقساط متبقية",
                        icon = Icons.Default.PersonSearch,
                        iconBg = Color(0xFFEDE7F6),
                        iconColor = Color(0xFF512DA8),
                        onClick = { onNavigateTab("debts") },
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "الجرد والتقارير العامة",
                        value = "تقرير شامل",
                        subtext = "تصدير وطباعة كشف الحساب",
                        icon = Icons.Default.Assessment,
                        iconBg = Color(0xFFE0F2F1),
                        iconColor = Color(0xFF00695C),
                        onClick = { onNavigateTab("general-audit") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Quick Navigation Actions
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "الوصول السريع للأقسام",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    QuickActionButton("تسجيل دخل مريض جديد", Icons.Default.PersonAdd, Color(0xFF2E7D32)) { onNavigateTab("daily-income") }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    QuickActionButton("إضافة شغل معمل أسنان", Icons.Default.AddBusiness, Color(0xFF1565C0)) { onNavigateTab("labs") }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    QuickActionButton("تسجيل شراء مستلزمات ومواد", Icons.Default.ShoppingCart, Color(0xFFE65100)) { onNavigateTab("supplies") }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    QuickActionButton("إدخال خرجيات ومصاريف العيادة", Icons.Default.MoneyOff, Color(0xFFC62828)) { onNavigateTab("expenses") }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    QuickActionButton("متابعة أقساط وديون المرضى والرسائل التلقائية", Icons.Default.Sms, Color(0xFF512DA8)) { onNavigateTab("debts") }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtext,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    text: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
    }
}

private fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%,.0f", amount)
}
