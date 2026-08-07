package com.example.dentalaccounting.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalaccounting.data.DentalLabEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DentalLabsSection(
    items: List<DentalLabEntity>,
    onAddItem: (DentalLabEntity) -> Unit,
    onUpdateItem: (DentalLabEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedStatusFilter by remember { mutableStateOf("الكل") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DentalLabEntity?>(null) }

    val filteredItems = items.filter { item ->
        val matchesStatus = if (selectedStatusFilter == "الكل") true else item.status == selectedStatusFilter
        val matchesSearch = item.labName.contains(searchQuery, ignoreCase = true) ||
                item.patientName.contains(searchQuery, ignoreCase = true) ||
                item.workType.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "إضافة معمل") },
                text = { Text("إضافة معاملة معمل") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_lab_fab")
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
                placeholder = { Text("بحث باسم المعمل، المريض، نوع التركيبة...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Status Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf("الكل", "قيد التصنيع", "تم الاستلام", "متأخر")
                items(filters) { filter ->
                    val isSelected = selectedStatusFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedStatusFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            // Summary row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "معاملات المعامل (${filteredItems.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "مستحقات المعامل: ${formatMoney(filteredItems.sumOf { it.remainingAmount })} ريال",
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
                    Text(text = "لا توجد معاملات معامل مسجلة.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        DentalLabCard(
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
        DentalLabDialog(
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
private fun DentalLabCard(
    item: DentalLabEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val statusBg = when (item.status) {
        "تم الاستلام" -> Color(0xFFE8F5E9)
        "متأخر" -> Color(0xFFFFEBEE)
        else -> Color(0xFFFFF3E0)
    }
    val statusColor = when (item.status) {
        "تم الاستلام" -> Color(0xFF2E7D32)
        "متأخر" -> Color(0xFFC62828)
        else -> Color(0xFFE65100)
    }

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
                    Text(text = item.labName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${item.workType} (${item.teethCount} أسنان) - مريض: ${item.patientName}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(color = statusBg, shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = item.status,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
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
                    Text("إجمالي المعمل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.totalAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("المدفوع", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.paidAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column {
                    Text("المتبقي للمعمل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.remainingAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "إرسال: ${item.sendDate} | استلام متوقع: ${item.receiveDate}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    if (item.labPhone.isNotBlank()) {
                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.labPhone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "اتصال بالمعمل", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        }
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "حذف", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DentalLabDialog(
    initialItem: DentalLabEntity?,
    onDismiss: () -> Unit,
    onSave: (DentalLabEntity) -> Unit
) {
    var labName by remember { mutableStateOf(initialItem?.labName ?: "") }
    var workType by remember { mutableStateOf(initialItem?.workType ?: "") }
    var teethCountText by remember { mutableStateOf(initialItem?.teethCount?.toString() ?: "1") }
    var totalAmountText by remember { mutableStateOf(initialItem?.totalAmount?.toInt()?.toString() ?: "") }
    var paidAmountText by remember { mutableStateOf(initialItem?.paidAmount?.toInt()?.toString() ?: "") }
    var patientName by remember { mutableStateOf(initialItem?.patientName ?: "") }
    var labPhone by remember { mutableStateOf(initialItem?.labPhone ?: "") }
    var status by remember { mutableStateOf(initialItem?.status ?: "قيد التصنيع") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val total = totalAmountText.toDoubleOrNull() ?: 0.0
    val paid = paidAmountText.toDoubleOrNull() ?: 0.0
    val remaining = (total - paid).coerceAtLeast(0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "إضافة شغل معمل جديد" else "تعديل بيانات المعمل") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = labName,
                    onValueChange = { labName = it },
                    label = { Text("اسم المعمل") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = workType,
                    onValueChange = { workType = it },
                    label = { Text("نوع التركيبة (زيركون, سيراميك, طقم...)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("اسم المريض") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = teethCountText,
                        onValueChange = { teethCountText = it },
                        label = { Text("عدد الأسنان") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(0.6f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = totalAmountText,
                        onValueChange = { totalAmountText = it },
                        label = { Text("مبلغ المعمل") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = paidAmountText,
                        onValueChange = { paidAmountText = it },
                        label = { Text("الواصل للمعمل") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Text("المتبقي للمعمل: ${formatMoney(remaining)} ريال", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 12.sp)

                OutlinedTextField(
                    value = labPhone,
                    onValueChange = { labPhone = it },
                    label = { Text("هاتف المعمل") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات / درجة اللون") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = dateFormat.format(Date())
                val newItem = DentalLabEntity(
                    id = initialItem?.id ?: 0,
                    labName = labName.ifBlank { "معمل أسنان" },
                    workType = workType.ifBlank { "تركيبة" },
                    teethCount = teethCountText.toIntOrNull() ?: 1,
                    totalAmount = total,
                    paidAmount = paid,
                    remainingAmount = remaining,
                    sendDate = initialItem?.sendDate ?: today,
                    receiveDate = initialItem?.receiveDate ?: today,
                    patientName = patientName,
                    patientPhone = "",
                    labPhone = labPhone,
                    notes = notes,
                    status = status
                )
                onSave(newItem)
            }) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("إلغاء") }
        }
    )
}

private fun formatMoney(amount: Double): String {
    return String.format(Locale.US, "%,.0f", amount)
}
