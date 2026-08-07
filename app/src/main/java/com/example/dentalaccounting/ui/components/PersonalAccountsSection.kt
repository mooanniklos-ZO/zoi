package com.example.dentalaccounting.ui.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalaccounting.data.PersonalAccountEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PersonalAccountsSection(
    items: List<PersonalAccountEntity>,
    onAddItem: (PersonalAccountEntity) -> Unit,
    onUpdateItem: (PersonalAccountEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<PersonalAccountEntity?>(null) }

    val filteredItems = items.filter { item ->
        item.procedureName.contains(searchQuery, ignoreCase = true) ||
        item.notes.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "إضافة قيد") },
                text = { Text("إضافة عملية حسابية") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_personal_account_fab")
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
                placeholder = { Text("بحث باسم العملية أو الإجراء...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الحسابات الشخصية والعمليات (${filteredItems.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "لا توجد عمليات حسابية شخصية مسجلة.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        PersonalAccountCard(
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
        PersonalAccountDialog(
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
private fun PersonalAccountCard(
    item: PersonalAccountEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
                Text(text = item.procedureName, fontSize = 16.sp, fontWeight = FontWeight.Bold)

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
                    Text("الإجمالي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.totalAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("له", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.creditForHim)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
                Column {
                    Text("عليه", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.debitOnHim)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                }
                Column {
                    Text("المتبقي", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${formatMoney(item.remainingAmount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (item.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "ملاحظات: ${item.notes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun PersonalAccountDialog(
    initialItem: PersonalAccountEntity?,
    onDismiss: () -> Unit,
    onSave: (PersonalAccountEntity) -> Unit
) {
    var procedureName by remember { mutableStateOf(initialItem?.procedureName ?: "") }
    var totalAmountText by remember { mutableStateOf(initialItem?.totalAmount?.toInt()?.toString() ?: "") }
    var creditText by remember { mutableStateOf(initialItem?.creditForHim?.toInt()?.toString() ?: "") }
    var debitText by remember { mutableStateOf(initialItem?.debitOnHim?.toInt()?.toString() ?: "") }
    var phone by remember { mutableStateOf(initialItem?.phone ?: "") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val total = totalAmountText.toDoubleOrNull() ?: 0.0
    val credit = creditText.toDoubleOrNull() ?: 0.0
    val debit = debitText.toDoubleOrNull() ?: 0.0
    val remaining = (total - credit + debit).coerceAtLeast(0.0)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "إضافة قيد حسابي جديد" else "تعديل القيد الحسابي") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = procedureName,
                    onValueChange = { procedureName = it },
                    label = { Text("اسم الإجراء أو العملية") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = totalAmountText,
                    onValueChange = { totalAmountText = it },
                    label = { Text("إجمالي المبلغ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = creditText,
                        onValueChange = { creditText = it },
                        label = { Text("له (دائن)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = debitText,
                        onValueChange = { debitText = it },
                        label = { Text("عليه (مدين)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Text("المتبقي: ${formatMoney(remaining)} ريال", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الهاتف") },
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
                val newItem = PersonalAccountEntity(
                    id = initialItem?.id ?: 0,
                    procedureName = procedureName.ifBlank { "عملية حسابية" },
                    totalAmount = total,
                    creditForHim = credit,
                    debitOnHim = debit,
                    remainingAmount = remaining,
                    date = initialItem?.date ?: today,
                    phone = phone,
                    autoMessageText = "",
                    scheduledSendTime = today,
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
