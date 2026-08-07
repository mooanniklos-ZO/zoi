package com.example.dentalaccounting.data

import kotlinx.coroutines.flow.Flow

class ClinicRepository(private val dao: ClinicDao) {

    // Daily Income
    val allDailyIncome: Flow<List<DailyIncomeEntity>> = dao.getAllDailyIncome()
    suspend fun insertDailyIncome(item: DailyIncomeEntity) = dao.insertDailyIncome(item)
    suspend fun updateDailyIncome(item: DailyIncomeEntity) = dao.updateDailyIncome(item)
    suspend fun deleteDailyIncome(id: Long) = dao.deleteDailyIncome(id)

    // Dental Labs
    val allDentalLabs: Flow<List<DentalLabEntity>> = dao.getAllDentalLabs()
    suspend fun insertDentalLab(item: DentalLabEntity) = dao.insertDentalLab(item)
    suspend fun updateDentalLab(item: DentalLabEntity) = dao.updateDentalLab(item)
    suspend fun deleteDentalLab(id: Long) = dao.deleteDentalLab(id)

    // Dental Suppliers
    val allDentalSuppliers: Flow<List<DentalSupplierEntity>> = dao.getAllDentalSuppliers()
    suspend fun insertDentalSupplier(item: DentalSupplierEntity) = dao.insertDentalSupplier(item)
    suspend fun updateDentalSupplier(item: DentalSupplierEntity) = dao.updateDentalSupplier(item)
    suspend fun deleteDentalSupplier(id: Long) = dao.deleteDentalSupplier(id)

    // Expenses
    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()
    suspend fun insertExpense(item: ExpenseEntity) = dao.insertExpense(item)
    suspend fun updateExpense(item: ExpenseEntity) = dao.updateExpense(item)
    suspend fun deleteExpense(id: Long) = dao.deleteExpense(id)

    // Patient Debts
    val allPatientDebts: Flow<List<PatientDebtEntity>> = dao.getAllPatientDebts()
    suspend fun insertPatientDebt(item: PatientDebtEntity) = dao.insertPatientDebt(item)
    suspend fun updatePatientDebt(item: PatientDebtEntity) = dao.updatePatientDebt(item)
    suspend fun deletePatientDebt(id: Long) = dao.deletePatientDebt(id)

    // Personal Accounts
    val allPersonalAccounts: Flow<List<PersonalAccountEntity>> = dao.getAllPersonalAccounts()
    suspend fun insertPersonalAccount(item: PersonalAccountEntity) = dao.insertPersonalAccount(item)
    suspend fun updatePersonalAccount(item: PersonalAccountEntity) = dao.updatePersonalAccount(item)
    suspend fun deletePersonalAccount(id: Long) = dao.deletePersonalAccount(id)
}
