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
import com.example.dentalaccounting.data.DentalSupplierEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DentalSuppliesSection(
    items: List<DentalSupplierEntity>,
    onAddItem: (DentalSupplierEntity) -> Unit,
    onUpdateItem: (DentalSupplierEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DentalSupplierEntity?>(null) }

    val filteredItems = items.filter { item ->
        item.storeName.contains(searchQuery, ignoreCase = true) ||
        item.materialsType.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "شراء مواد") },
                text = { Text("تسجيل فاتورة مستلزمات") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_supplier_fab")
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
                placeholder = { Text("بحث باسم الشركة أو نوع المواد...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Header Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "فواتير شركات ومحلات المواد (${filteredItems.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ديون الموردين: ${formatMoney(filteredItems.sumOf { it.remainingAmount })} ريال",
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
                    Text(text = "لا توجد فواتير مستلزمات مسجلة.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        SupplierCard(
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
        SupplierDialog(
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
private fun SupplierCard(
    item: DentalSupplierEntity,
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
                    Text(text = item.storeName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = item.materialsType, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                    Text("إجمالي الفاتورة", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.totalAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("المبلغ المسدد", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.paidAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column {
                    Text("المتبقي للمحل", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.remainingAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "تاريخ الشراء: ${item.purchaseDate}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                if (item.storePhone.isNotBlank()) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.storePhone}"))
                            context.startActivity(intent)
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "اتصال", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("اتصال بالمحل", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SupplierDialog(
    initialItem: DentalSupplierEntity?,
    onDismiss: () -> Unit,
    onSave: (DentalSupplierEntity) -> Unit
) {
    var storeName by remember { mutableStateOf(initialItem?.storeName ?: "") }
    var materialsType by remember { mutableStateOf(initialItem?.materialsType ?: "") }
    var totalAmountText by remember { mutableStateOf(initialItem?.totalAmount?.toInt()?.toString() ?: "") }
    var paidAmountText by remember { mutableStateOf(initialItem?.paidAmount?.toInt()?.toString() ?: "") }
    var storePhone by remember { mutableStateOf(initialItem?.storePhone ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val total = totalAmountText.toDoubleOrNull() ?: 0.0
    val paid = paidAmountText.toDoubleOrNull() ?: 0.0
    val remaining = (total - paid).coerceAtLeast(0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "تسجيل فاتورة شراء مواد" else "تعديل الفاتورة") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = storeName,
                    onValueChange = { storeName = it },
                    label = { Text("اسم الشركة أو المحل") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = materialsType,
                    onValueChange = { materialsType = it },
                    label = { Text("أنواع المواد (بنج, حشوات, إبر...)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = totalAmountText,
                        onValueChange = { totalAmountText = it },
                        label = { Text("إجمالي الفاتورة") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = paidAmountText,
                        onValueChange = { paidAmountText = it },
                        label = { Text("المسدد للمحل") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Text("المتبقي للمحل: ${formatMoney(remaining)} ريال", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 12.sp)

                OutlinedTextField(
                    value = storePhone,
                    onValueChange = { storePhone = it },
                    label = { Text("رقم هاتف المحل") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
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
                val newItem = DentalSupplierEntity(
                    id = initialItem?.id ?: 0,
                    storeName = storeName.ifBlank { "محل مستلزمات" },
                    materialsType = materialsType.ifBlank { "مواد عيادة" },
                    totalAmount = total,
                    paidAmount = paid,
                    remainingAmount = remaining,
                    purchaseDate = initialItem?.purchaseDate ?: today,
                    storePhone = storePhone,
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
