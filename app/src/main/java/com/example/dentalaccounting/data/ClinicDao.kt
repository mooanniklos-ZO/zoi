package com.example.dentalaccounting.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicDao {

    // Daily Income
    @Query("SELECT * FROM daily_income ORDER BY id DESC")
    fun getAllDailyIncome(): Flow<List<DailyIncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyIncome(item: DailyIncomeEntity)

    @Update
    suspend fun updateDailyIncome(item: DailyIncomeEntity)

    @Query("DELETE FROM daily_income WHERE id = :id")
    suspend fun deleteDailyIncome(id: Long)

    // Dental Labs
    @Query("SELECT * FROM dental_labs ORDER BY id DESC")
    fun getAllDentalLabs(): Flow<List<DentalLabEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDentalLab(item: DentalLabEntity)

    @Update
    suspend fun updateDentalLab(item: DentalLabEntity)

    @Query("DELETE FROM dental_labs WHERE id = :id")
    suspend fun deleteDentalLab(id: Long)

    // Dental Suppliers
    @Query("SELECT * FROM dental_suppliers ORDER BY id DESC")
    fun getAllDentalSuppliers(): Flow<List<DentalSupplierEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDentalSupplier(item: DentalSupplierEntity)

    @Update
    suspend fun updateDentalSupplier(item: DentalSupplierEntity)

    @Query("DELETE FROM dental_suppliers WHERE id = :id")
    suspend fun deleteDentalSupplier(id: Long)

    // Expenses
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(item: ExpenseEntity)

    @Update
    suspend fun updateExpense(item: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpense(id: Long)

    // Patient Debts
    @Query("SELECT * FROM patient_debts ORDER BY id DESC")
    fun getAllPatientDebts(): Flow<List<PatientDebtEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatientDebt(item: PatientDebtEntity)

    @Update
    suspend fun updatePatientDebt(item: PatientDebtEntity)

    @Query("DELETE FROM patient_debts WHERE id = :id")
    suspend fun deletePatientDebt(id: Long)

    // Personal Accounts
    @Query("SELECT * FROM personal_accounts ORDER BY id DESC")
    fun getAllPersonalAccounts(): Flow<List<PersonalAccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalAccount(item: PersonalAccountEntity)

    @Update
    suspend fun updatePersonalAccount(item: PersonalAccountEntity)

    @Query("DELETE FROM personal_accounts WHERE id = :id")
    suspend fun deletePersonalAccount(id: Long)
}
