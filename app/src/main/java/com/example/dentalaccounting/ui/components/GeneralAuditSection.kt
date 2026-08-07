package com.example.dentalaccounting.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalaccounting.data.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GeneralAuditSection(
    dailyIncome: List<DailyIncomeEntity>,
    expenses: List<ExpenseEntity>,
    dentalLabs: List<DentalLabEntity>,
    dentalSupplies: List<DentalSupplierEntity>,
    patientDebts: List<PatientDebtEntity>,
    personalAccounts: List<PersonalAccountEntity>,
    modifier: Modifier = Modifier
) {
    var showPrintPreview by remember { mutableStateOf(false) }

    val totalIncomePaid = dailyIncome.sumOf { it.paidAmount }
    val totalIncomeAgreed = dailyIncome.sumOf { it.totalAmount }
    val totalLabPaid = dentalLabs.sumOf { it.paidAmount }
    val totalLabRemaining = dentalLabs.sumOf { it.remainingAmount }
    val totalSupplierPaid = dentalSupplies.sumOf { it.paidAmount }
    val totalSupplierRemaining = dentalSupplies.sumOf { it.remainingAmount }
    val totalExpenses = expenses.sumOf { it.totalExpense }
    val totalPatientDebts = patientDebts.sumOf { it.remainingAmount }

    val totalCostsPaid = totalLabPaid + totalSupplierPaid + totalExpenses
    val netProfit = totalIncomePaid - totalCostsPaid

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تقرير الجرد العام والتدقيق المالي الدوري",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Button(
                    onClick = { showPrintPreview = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("export_report_button")
                ) {
                    Icon(Icons.Default.Print, contentDescription = "طباعة الكشف", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("معاينة الكشف", fontSize = 13.sp)
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الخلاصة المالية للعيادة - د. مالك الرميمة",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuditRow(
                        label = "إجمالي إيرادات المرضى المقبوضة نقدياً",
                        value = "${formatMoney(totalIncomePaid)} ريال",
                        valueColor = Color(0xFF2E7D32)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    AuditRow(
                        label = "إجمالي مدفوعات المعامل",
                        value = "${formatMoney(totalLabPaid)} ريال",
                        valueColor = Color(0xFF1565C0)
                    )
                    AuditRow(
                        label = "إجمالي مدفوعات المستلزمات والمواد",
                        value = "${formatMoney(totalSupplierPaid)} ريال",
                        valueColor = Color(0xFFE65100)
                    )
                    AuditRow(
                        label = "إجمالي الخرجيات والمصاريف التشغيلية",
                        value = "${formatMoney(totalExpenses)} ريال",
                        valueColor = Color(0xFFC62828)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    AuditRow(
                        label = "مجموع كافة المصاريف والتكاليف",
                        value = "${formatMoney(totalCostsPaid)} ريال",
                        valueColor = Color(0xFFC62828),
                        isBold = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (netProfit >= 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "صافي الأرباح النقدية المتبقية بالخزينة:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (netProfit >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            Text(
                                text = "${formatMoney(netProfit)} ريال",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (netProfit >= 0) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "الالتزامات والديون المعلقة",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    AuditRow(
                        label = "إجمالي ديون المرضى المستحقة للعيادة",
                        value = "${formatMoney(totalPatientDebts)} ريال",
                        valueColor = Color(0xFF512DA8),
                        isBold = true
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    AuditRow(
                        label = "المتبقي للمعامل كمستحقات غير مسددة",
                        value = "${formatMoney(totalLabRemaining)} ريال",
                        valueColor = Color(0xFFC62828)
                    )
                    AuditRow(
                        label = "المتبقي لموردي المواد كمستحقات غير مسددة",
                        value = "${formatMoney(totalSupplierRemaining)} ريال",
                        valueColor = Color(0xFFC62828)
                    )
                }
            }
        }
    }

    if (showPrintPreview) {
        AlertDialog(
            onDismissRequest = { showPrintPreview = false },
            title = { Text("معاينة كشف الحساب المالي للعيادة") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("عيادة الأسنان - د. مالك الرميمة", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                    Text("تاريخ التقرير: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}", fontSize = 12.sp)

                    HorizontalDivider()

                    Text("• المقبوضات: ${formatMoney(totalIncomePaid)} ريال")
                    Text("• المصاريف التشغيلية: ${formatMoney(totalExpenses)} ريال")
                    Text("• المعامل والمستلزمات: ${formatMoney(totalLabPaid + totalSupplierPaid)} ريال")
                    Text("• صافي الأرباح: ${formatMoney(netProfit)} ريال", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Text("• ديون المرضى المتبقية: ${formatMoney(totalPatientDebts)} ريال", color = Color(0xFFC62828))
                }
            },
            confirmButton = {
                Button(onClick = { showPrintPreview = false }) {
                    Text("إغلاق المعاينة")
                }
            }
        )
    }
}

@Composable
private fun AuditRow(
    label: String,
    value: String,
    valueColor: Color = Color.Unspecified,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

private fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%,.0f", amount)
}
