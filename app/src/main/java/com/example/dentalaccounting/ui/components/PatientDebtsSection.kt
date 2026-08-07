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
import com.example.dentalaccounting.data.PatientDebtEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PatientDebtsSection(
    items: List<PatientDebtEntity>,
    onAddItem: (PatientDebtEntity) -> Unit,
    onUpdateItem: (PatientDebtEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<PatientDebtEntity?>(null) }

    val filteredItems = items.filter { item ->
        item.patientName.contains(searchQuery, ignoreCase = true) ||
        item.area.contains(searchQuery, ignoreCase = true) ||
        item.guarantorName.contains(searchQuery, ignoreCase = true) ||
        item.phone.contains(searchQuery)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "تسجيل قسط") },
                text = { Text("تسجيل قسط/دين مريض") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_debt_fab")
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
                placeholder = { Text("بحث باسم المريض، المنطقة، الضامن، الهاتف...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
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
                    text = "سجل أقساط وديون المرضى (${filteredItems.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "إجمالي الديون: ${formatMoney(filteredItems.sumOf { it.remainingAmount })} ريال",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
            }

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "لا توجد ديون أو أقساط مسجلة.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        DebtCard(
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
        DebtDialog(
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
private fun DebtCard(
    item: PatientDebtEntity,
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
                Column {
                    Text(text = item.patientName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    if (item.area.isNotBlank() || item.guarantorName.isNotBlank()) {
                        Text(
                            text = "المنطقة: ${item.area.ifBlank { "-" }} | الضامن: ${item.guarantorName.ifBlank { "-" }}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("إجمالي الدين", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.totalAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("الواصل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.paidAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column {
                    Text("المتبقي كدين", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.remainingAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
            }

            if (item.autoSmsText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "صيغة الرسالة التلقائية:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(text = item.autoSmsText, fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تاريخ الاستحقاق: ${item.scheduledDate}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (item.phone.isNotBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${item.phone}"))
                                intent.putExtra("sms_body", item.autoSmsText)
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Sms, contentDescription = "SMS", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SMS", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${item.phone}&text=${Uri.encode(item.autoSmsText)}"))
                                context.startActivity(intent)
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Icon(Icons.Default.Message, contentDescription = "واتساب", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تذكير واتساب", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DebtDialog(
    initialItem: PatientDebtEntity?,
    onDismiss: () -> Unit,
    onSave: (PatientDebtEntity) -> Unit
) {
    var patientName by remember { mutableStateOf(initialItem?.patientName ?: "") }
    var area by remember { mutableStateOf(initialItem?.area ?: "") }
    var guarantorName by remember { mutableStateOf(initialItem?.guarantorName ?: "") }
    var totalAmountText by remember { mutableStateOf(initialItem?.totalAmount?.toInt()?.toString() ?: "") }
    var paidAmountText by remember { mutableStateOf(initialItem?.paidAmount?.toInt()?.toString() ?: "") }
    var phone by remember { mutableStateOf(initialItem?.phone ?: "") }
    var autoSmsText by remember { mutableStateOf(initialItem?.autoSmsText ?: "") }
    var scheduledDate by remember { mutableStateOf(initialItem?.scheduledDate ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val total = totalAmountText.toDoubleOrNull() ?: 0.0
    val paid = paidAmountText.toDoubleOrNull() ?: 0.0
    val remaining = (total - paid).coerceAtLeast(0.0)

    // Auto generate SMS text when fields change if empty
    LaunchedEffect(patientName, remaining) {
        if (autoSmsText.isBlank()) {
            autoSmsText = "عزيزي المريض ${patientName.ifBlank { "[الاسم]" }}، نود تذكيركم بموعد سداد القسط المتبقي قدره ${formatMoney(remaining)} ريال لعيادة الدكتور مالك الرميمة. تحياتنا."
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "تسجيل قسط ودين جديد" else "تعديل القسط") },
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = area,
                        onValueChange = { area = it },
                        label = { Text("منطقة المريض") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = guarantorName,
                        onValueChange = { guarantorName = it },
                        label = { Text("اسم الضامن/المعرف") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = totalAmountText,
                        onValueChange = { totalAmountText = it },
                        label = { Text("إجمالي المبلغ") },
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
                Text("الدين المتبقي: ${formatMoney(remaining)} ريال", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 12.sp)

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم هاتف المريض") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = autoSmsText,
                    onValueChange = { autoSmsText = it },
                    label = { Text("نص التذكير التلقائي (رسالة)") },
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
            Button(onClick = {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = dateFormat.format(Date())
                val newItem = PatientDebtEntity(
                    id = initialItem?.id ?: 0,
                    patientName = patientName.ifBlank { "مريض" },
                    area = area,
                    guarantorName = guarantorName,
                    totalAmount = total,
                    paidAmount = paid,
                    remainingAmount = remaining,
                    date = initialItem?.date ?: today,
                    phone = phone,
                    autoSmsText = autoSmsText,
                    scheduledDate = if (scheduledDate.isBlank()) today else scheduledDate,
                    notes = notes
                )
                onSave(newItem)
            }) { Text("حفظ") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}

private fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%,.0f", amount)
}
