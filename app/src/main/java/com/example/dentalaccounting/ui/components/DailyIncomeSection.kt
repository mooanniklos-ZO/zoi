package com.example.dentalaccounting.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalaccounting.data.DailyIncomeEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyIncomeSection(
    items: List<DailyIncomeEntity>,
    onAddItem: (DailyIncomeEntity) -> Unit,
    onUpdateItem: (DailyIncomeEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DailyIncomeEntity?>(null) }

    val filteredItems = items.filter { item ->
        item.patientName.contains(searchQuery, ignoreCase = true) ||
        item.caseType.contains(searchQuery, ignoreCase = true) ||
        item.phone.contains(searchQuery) ||
        item.diagnosis.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "إضافة") },
                text = { Text("تسجيل حالة جديدة") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_daily_income_fab")
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("بحث باسم المريض، نوع الحالة، أو الهاتف...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "مسح")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Header summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "سجل حالات وحركة المرضى (${filteredItems.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "الإجمالي: ${formatMoney(filteredItems.sumOf { it.paidAmount })} ريال",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (searchQuery.isEmpty()) "لا توجد حركات دخل يومية مسجلة حتى الآن." else "لا توجد نتائج مطابقة لبحثك.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        DailyIncomeCard(
                            item = item,
                            onEdit = {
                                editingItem = item
                                showAddEditDialog = true
                            },
                            onDelete = { onDeleteItem(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddEditDialog) {
        DailyIncomeDialog(
            initialItem = editingItem,
            onDismiss = { showAddEditDialog = false },
            onSave = { savedItem ->
                if (editingItem == null) {
                    onAddItem(savedItem)
                } else {
                    onUpdateItem(savedItem)
                }
                showAddEditDialog = false
            }
        )
    }
}

@Composable
private fun DailyIncomeCard(
    item: DailyIncomeEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = item.caseType,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.patientName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (item.diagnosis.isNotBlank()) {
                Text(
                    text = "التشخيص: ${item.diagnosis}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item.treatment.isNotBlank()) {
                Text(
                    text = "المعالجة: ${item.treatment}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "الإجمالي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${formatMoney(item.totalAmount)} ريال", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "الواصل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "${formatMoney(item.paidAmount)} ريال", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column {
                    Text(text = "المتبقي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = "${formatMoney(item.remainingAmount)} ريال",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.remainingAmount > 0) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            if (item.phone.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "رقم الهاتف: ${item.phone}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phone}"))
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "اتصال", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "اتصال", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                val msg = "مرحباً ${item.patientName}، تذكير بموعد عيادة الأسنان (د. مالك الرميمة) بخصوص ${item.caseType}."
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${item.phone}&text=${Uri.encode(msg)}"))
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Icon(Icons.Default.Message, contentDescription = "واتساب", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "واتساب", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyIncomeDialog(
    initialItem: DailyIncomeEntity?,
    onDismiss: () -> Unit,
    onSave: (DailyIncomeEntity) -> Unit
) {
    var patientName by remember { mutableStateOf(initialItem?.patientName ?: "") }
    var caseType by remember { mutableStateOf(initialItem?.caseType ?: "حشو عصب") }
    var totalAmountText by remember { mutableStateOf(initialItem?.totalAmount?.toInt()?.toString() ?: "") }
    var paidAmountText by remember { mutableStateOf(initialItem?.paidAmount?.toInt()?.toString() ?: "") }
    var diagnosis by remember { mutableStateOf(initialItem?.diagnosis ?: "") }
    var treatment by remember { mutableStateOf(initialItem?.treatment ?: "") }
    var phone by remember { mutableStateOf(initialItem?.phone ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val total = totalAmountText.toDoubleOrNull() ?: 0.0
    val paid = paidAmountText.toDoubleOrNull() ?: 0.0
    val remaining = (total - paid).coerceAtLeast(0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (initialItem == null) "تسجيل مريض ودخل جديد" else "تعديل سجل الدخل")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("اسم المريض") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = caseType,
                    onValueChange = { caseType = it },
                    label = { Text("نوع الحالة (حشو, قلع, تركيبات...)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = totalAmountText,
                        onValueChange = { totalAmountText = it },
                        label = { Text("إجمالي الاتفاق") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = paidAmountText,
                        onValueChange = { paidAmountText = it },
                        label = { Text("المبلغ الواصل") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = "المتبقي: ${formatMoney(remaining)} ريال",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (remaining > 0) Color(0xFFC62828) else Color(0xFF2E7D32)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الهاتف") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = diagnosis,
                    onValueChange = { diagnosis = it },
                    label = { Text("التشخيص") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = treatment,
                    onValueChange = { treatment = it },
                    label = { Text("المعالجة المنفذة") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateStr = initialItem?.date ?: dateFormat.format(Date())

                    val newItem = DailyIncomeEntity(
                        id = initialItem?.id ?: 0,
                        patientName = patientName.ifBlank { "مريض بدون اسم" },
                        caseType = caseType.ifBlank { "علاج أسنان" },
                        totalAmount = total,
                        paidAmount = paid,
                        remainingAmount = remaining,
                        diagnosis = diagnosis,
                        treatment = treatment,
                        phone = phone,
                        notes = notes,
                        date = dateStr
                    )
                    onSave(newItem)
                }
            ) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

private fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%,.0f", amount)
}
