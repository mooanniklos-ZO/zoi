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
import com.example.dentalaccounting.data.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpensesSection(
    items: List<ExpenseEntity>,
    onAddItem: (ExpenseEntity) -> Unit,
    onUpdateItem: (ExpenseEntity) -> Unit,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ExpenseEntity?>(null) }

    val totalNurse = items.sumOf { it.nurseExpense }
    val totalHome = items.sumOf { it.homeKitchenExpense }
    val totalFamily = items.sumOf { it.familyExpense }
    val totalGeneral = items.sumOf { it.generalExpense }
    val totalFuel = items.sumOf { it.fuelMaintenanceExpense }
    val grandTotal = items.sumOf { it.totalExpense }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    editingItem = null
                    showAddEditDialog = true
                },
                icon = { Icon(Icons.Default.Add, contentDescription = "تسجيل خرجية") },
                text = { Text("تسجيل خرجيات جديدة") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_expense_fab")
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
            // Category Breakdown Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "إجمالي الخرجيات والمصاريف المسجلة: ${formatMoney(grandTotal)} ريال",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ExpenseCategoryText("الممرضة", totalNurse)
                        ExpenseCategoryText("البيت والمطبخ", totalHome)
                        ExpenseCategoryText("الأسرة والأولاد", totalFamily)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ExpenseCategoryText("نثرية العيادة", totalGeneral)
                        ExpenseCategoryText("بترول وصيانة المولد", totalFuel)
                    }
                }
            }

            Text(
                text = "سجل الخرجيات اليومية (${items.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "لا توجد خرجيات مسجلة.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items, key = { it.id }) { item ->
                        ExpenseCard(
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
        ExpenseDialog(
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
private fun ExpenseCategoryText(label: String, amount: Double) {
    Column {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
        Text(text = "${formatMoney(amount)} ريال", fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ExpenseCard(
    item: ExpenseEntity,
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = item.date, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "المجموع: ${formatMoney(item.totalExpense)} ريال",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828)
                )
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
                    Text("الممرضة", fontSize = 10.sp)
                    Text("${formatMoney(item.nurseExpense)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("البيت", fontSize = 10.sp)
                    Text("${formatMoney(item.homeKitchenExpense)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("الأولاد", fontSize = 10.sp)
                    Text("${formatMoney(item.familyExpense)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("نثرية", fontSize = 10.sp)
                    Text("${formatMoney(item.generalExpense)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("المولد/صيانة", fontSize = 10.sp)
                    Text("${formatMoney(item.fuelMaintenanceExpense)}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (item.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "ملاحظات: ${item.notes}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
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

@Composable
private fun ExpenseDialog(
    initialItem: ExpenseEntity?,
    onDismiss: () -> Unit,
    onSave: (ExpenseEntity) -> Unit
) {
    var nurse by remember { mutableStateOf(initialItem?.nurseExpense?.toInt()?.toString() ?: "0") }
    var home by remember { mutableStateOf(initialItem?.homeKitchenExpense?.toInt()?.toString() ?: "0") }
    var family by remember { mutableStateOf(initialItem?.familyExpense?.toInt()?.toString() ?: "0") }
    var general by remember { mutableStateOf(initialItem?.generalExpense?.toInt()?.toString() ?: "0") }
    var fuel by remember { mutableStateOf(initialItem?.fuelMaintenanceExpense?.toInt()?.toString() ?: "0") }
    var notes by remember { mutableStateOf(initialItem?.notes ?: "") }

    val nVal = nurse.toDoubleOrNull() ?: 0.0
    val hVal = home.toDoubleOrNull() ?: 0.0
    val fVal = family.toDoubleOrNull() ?: 0.0
    val gVal = general.toDoubleOrNull() ?: 0.0
    val fuelVal = fuel.toDoubleOrNull() ?: 0.0
    val total = nVal + hVal + fVal + gVal + fuelVal

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialItem == null) "تسجيل خرجية جديدة" else "تعديل الخرجية") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nurse,
                        onValueChange = { nurse = it },
                        label = { Text("الممرضة") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = home,
                        onValueChange = { home = it },
                        label = { Text("البيت والمطبخ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = family,
                        onValueChange = { family = it },
                        label = { Text("الزوجة والأولاد") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = general,
                        onValueChange = { general = it },
                        label = { Text("نثرية العيادة") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = fuel,
                    onValueChange = { fuel = it },
                    label = { Text("وقود وبترول وصيانة المولد") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("المجموع الكلي للخرجية: ${formatMoney(total)} ريال", fontWeight = FontWeight.Bold, color = Color(0xFFC62828), fontSize = 13.sp)

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات / تفاصيل") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val today = dateFormat.format(Date())
                val newItem = ExpenseEntity(
                    id = initialItem?.id ?: 0,
                    nurseExpense = nVal,
                    homeKitchenExpense = hVal,
                    familyExpense = fVal,
                    generalExpense = gVal,
                    fuelMaintenanceExpense = fuelVal,
                    totalExpense = total,
                    date = initialItem?.date ?: today,
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
