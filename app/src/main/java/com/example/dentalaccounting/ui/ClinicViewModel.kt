package com.example.dentalaccounting.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dentalaccounting.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "assistant"
    val text: String,
    val timestamp: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
)

class ClinicViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = ClinicRepository(db.clinicDao())

    // Active Navigation Tab
    private val _activeTab = MutableStateFlow("dashboard")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    // AI Assistant Dialog visibility
    private val _isAiAssistantOpen = MutableStateFlow(false)
    val isAiAssistantOpen: StateFlow<Boolean> = _isAiAssistantOpen.asStateFlow()

    fun setAiAssistantOpen(isOpen: Boolean) {
        _isAiAssistantOpen.value = isOpen
    }

    // AI Messages
    private val _aiMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "assistant",
                text = "أهلاً بك دكتور مالك الرميمة! أنا مساعدك المالي الذكي لعيادة الأسنان. يمكنني مساعدتك في تحليل الإيرادات، حساب تكاليف المعامل والمستلزمات، ومتابعة ديون المرضى والصافي المالي. كيف يمكنني مساعدتك اليوم؟"
            )
        )
    )
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Data Flows from Room Repository
    val dailyIncomeList: StateFlow<List<DailyIncomeEntity>> = repository.allDailyIncome
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dentalLabsList: StateFlow<List<DentalLabEntity>> = repository.allDentalLabs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dentalSuppliesList: StateFlow<List<DentalSupplierEntity>> = repository.allDentalSuppliers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expensesList: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val patientDebtsList: StateFlow<List<PatientDebtEntity>> = repository.allPatientDebts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val personalAccountsList: StateFlow<List<PersonalAccountEntity>> = repository.allPersonalAccounts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // CRUD operations - Daily Income
    fun addDailyIncome(item: DailyIncomeEntity) {
        viewModelScope.launch { repository.insertDailyIncome(item) }
    }
    fun updateDailyIncome(item: DailyIncomeEntity) {
        viewModelScope.launch { repository.updateDailyIncome(item) }
    }
    fun deleteDailyIncome(id: Long) {
        viewModelScope.launch { repository.deleteDailyIncome(id) }
    }

    // CRUD operations - Labs
    fun addDentalLab(item: DentalLabEntity) {
        viewModelScope.launch { repository.insertDentalLab(item) }
    }
    fun updateDentalLab(item: DentalLabEntity) {
        viewModelScope.launch { repository.updateDentalLab(item) }
    }
    fun deleteDentalLab(id: Long) {
        viewModelScope.launch { repository.deleteDentalLab(id) }
    }

    // CRUD operations - Supplies
    fun addDentalSupplier(item: DentalSupplierEntity) {
        viewModelScope.launch { repository.insertDentalSupplier(item) }
    }
    fun updateDentalSupplier(item: DentalSupplierEntity) {
        viewModelScope.launch { repository.updateDentalSupplier(item) }
    }
    fun deleteDentalSupplier(id: Long) {
        viewModelScope.launch { repository.deleteDentalSupplier(id) }
    }

    // CRUD operations - Expenses
    fun addExpense(item: ExpenseEntity) {
        viewModelScope.launch { repository.insertExpense(item) }
    }
    fun updateExpense(item: ExpenseEntity) {
        viewModelScope.launch { repository.updateExpense(item) }
    }
    fun deleteExpense(id: Long) {
        viewModelScope.launch { repository.deleteExpense(id) }
    }

    // CRUD operations - Patient Debts
    fun addPatientDebt(item: PatientDebtEntity) {
        viewModelScope.launch { repository.insertPatientDebt(item) }
    }
    fun updatePatientDebt(item: PatientDebtEntity) {
        viewModelScope.launch { repository.updatePatientDebt(item) }
    }
    fun deletePatientDebt(id: Long) {
        viewModelScope.launch { repository.deletePatientDebt(id) }
    }

    // CRUD operations - Personal Accounts
    fun addPersonalAccount(item: PersonalAccountEntity) {
        viewModelScope.launch { repository.insertPersonalAccount(item) }
    }
    fun updatePersonalAccount(item: PersonalAccountEntity) {
        viewModelScope.launch { repository.updatePersonalAccount(item) }
    }
    fun deletePersonalAccount(id: Long) {
        viewModelScope.launch { repository.deletePersonalAccount(id) }
    }

    // AI Prompt Handling
    fun sendAiPrompt(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = prompt)
        _aiMessages.value = _aiMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            // Build Contextual Analysis based on live financial state
            val income = dailyIncomeList.value.sumOf { it.paidAmount }
            val totalIncomeAgreed = dailyIncomeList.value.sumOf { it.totalAmount }
            val labExpenses = dentalLabsList.value.sumOf { it.paidAmount }
            val labRemaining = dentalLabsList.value.sumOf { it.remainingAmount }
            val supplierExpenses = dentalSuppliesList.value.sumOf { it.paidAmount }
            val supplierRemaining = dentalSuppliesList.value.sumOf { it.remainingAmount }
            val totalClinicExpenses = expensesList.value.sumOf { it.totalExpense }
            val totalPatientDebts = patientDebtsList.value.sumOf { it.remainingAmount }
            val netProfit = income - (labExpenses + supplierExpenses + totalClinicExpenses)

            val replyText = generateAssistantReply(
                prompt = prompt,
                income = income,
                totalIncomeAgreed = totalIncomeAgreed,
                labExpenses = labExpenses,
                labRemaining = labRemaining,
                supplierExpenses = supplierExpenses,
                supplierRemaining = supplierRemaining,
                totalClinicExpenses = totalClinicExpenses,
                totalPatientDebts = totalPatientDebts,
                netProfit = netProfit
            )

            _isAiThinking.value = false
            _aiMessages.value = _aiMessages.value + ChatMessage(sender = "assistant", text = replyText)
        }
    }

    private fun generateAssistantReply(
        prompt: String,
        income: Double,
        totalIncomeAgreed: Double,
        labExpenses: Double,
        labRemaining: Double,
        supplierExpenses: Double,
        supplierRemaining: Double,
        totalClinicExpenses: Double,
        totalPatientDebts: Double,
        netProfit: Double
    ): String {
        val p = prompt.lowercase()
        return when {
            p.contains("تقرير") || p.contains("جرد") || p.contains("ملخص") || p.contains("ارباح") || p.contains("أرباح") -> {
                """
                📊 **تقرير الجرد المالي للعيادة - د. مالك الرميمة**:
                
                • **إجمالي المقبوضات النقدیة**: ${formatMoney(income)} ريال
                • **المصاريف والخرجيات اليومية**: ${formatMoney(totalClinicExpenses)} ريال
                • **مدفوعات المعامل**: ${formatMoney(labExpenses)} ريال (متبقي للمعامل: ${formatMoney(labRemaining)} ريال)
                • **مدفوعات الموردين والمستلزمات**: ${formatMoney(supplierExpenses)} ريال (متبقي للموردين: ${formatMoney(supplierRemaining)} ريال)
                • **صافي الربح الفعلي الحالي**: ${formatMoney(netProfit)} ريال
                • **إجمالي ديون المرضى المتبقية**: ${formatMoney(totalPatientDebts)} ريال
                
                💡 *توصية*: متابعة الأقساط المتبقية لدى المرضى سيزيد السيولة بمبلغ ${formatMoney(totalPatientDebts)} ريال.
                """.trimIndent()
            }
            p.contains("ديون") || p.contains("اقساط") || p.contains("أقساط") -> {
                "إجمالي الديون المستحقة على المرضى حالياً هو **${formatMoney(totalPatientDebts)} ريال**. يمكنك استخدام خاصية التذكيرات التلقائية عبر الرسائل النصية/واتساب للتواصل معهم مباشرة."
            }
            p.contains("مصاريف") || p.contains("خرجيات") -> {
                "إجمالي مصاريف العيادة التشغيلية والخرجيات بلغ **${formatMoney(totalClinicExpenses)} ريال**. وتشمل راتب الممرضة، المطبخ، المستلزمات النثرية، وصيانة وقود المولد."
            }
            else -> {
                "بناءً على سجلات عيادة د. مالك الرميمة الحالية:\n- صافي الدخل الفعلي: ${formatMoney(netProfit)} ريال.\n- إجمالي المقبوضات: ${formatMoney(income)} ريال.\n- ديون المرضى: ${formatMoney(totalPatientDebts)} ريال.\nكيف ترغب في تحسين الأداء المالي للعيادة اليوم؟"
            }
        }
    }

    private fun formatMoney(amount: Double): String {
        return String.format(Locale.US, "%,.0f", amount)
    }
}
