package com.example.dentalaccounting.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_income")
data class DailyIncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientName: String,
    val caseType: String, // نوع الحالة (حشو عصب, قلع, تركيب, تقويم, تبييض)
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val diagnosis: String,
    val treatment: String,
    val phone: String,
    val notes: String,
    val date: String,
    val receiptImage: String? = null
)

@Entity(tableName = "dental_labs")
data class DentalLabEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val labName: String,
    val workType: String,
    val teethCount: Int,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val sendDate: String,
    val receiveDate: String,
    val patientName: String,
    val patientPhone: String,
    val labPhone: String,
    val notes: String,
    val status: String // "قيد التصنيع" | "تم الاستلام" | "متأخر"
)

@Entity(tableName = "dental_suppliers")
data class DentalSupplierEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val storeName: String,
    val materialsType: String,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val purchaseDate: String,
    val storePhone: String,
    val notes: String
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nurseExpense: Double,
    val homeKitchenExpense: Double,
    val familyExpense: Double,
    val generalExpense: Double,
    val fuelMaintenanceExpense: Double,
    val totalExpense: Double,
    val date: String,
    val notes: String
)

@Entity(tableName = "patient_debts")
data class PatientDebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientName: String,
    val area: String,
    val guarantorName: String,
    val totalAmount: Double,
    val paidAmount: Double,
    val remainingAmount: Double,
    val date: String,
    val phone: String,
    val autoSmsText: String,
    val scheduledDate: String,
    val notes: String
)

@Entity(tableName = "personal_accounts")
data class PersonalAccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val procedureName: String,
    val totalAmount: Double,
    val creditForHim: Double, // له
    val debitOnHim: Double,   // عليه
    val remainingAmount: Double,
    val date: String,
    val phone: String,
    val autoMessageText: String,
    val scheduledSendTime: String,
    val notes: String
)
