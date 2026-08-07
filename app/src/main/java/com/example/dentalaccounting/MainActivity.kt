package com.example.dentalaccounting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dentalaccounting.ui.ClinicViewModel
import com.example.dentalaccounting.ui.components.*
import com.example.dentalaccounting.ui.theme.DentalAccountingTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ClinicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DentalAccountingTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
                    val isAiOpen by viewModel.isAiAssistantOpen.collectAsStateWithLifecycle()
                    val aiMessages by viewModel.aiMessages.collectAsStateWithLifecycle()
                    val isAiThinking by viewModel.isAiThinking.collectAsStateWithLifecycle()

                    val dailyIncome by viewModel.dailyIncomeList.collectAsStateWithLifecycle()
                    val dentalLabs by viewModel.dentalLabsList.collectAsStateWithLifecycle()
                    val dentalSupplies by viewModel.dentalSuppliesList.collectAsStateWithLifecycle()
                    val expenses by viewModel.expensesList.collectAsStateWithLifecycle()
                    val patientDebts by viewModel.patientDebtsList.collectAsStateWithLifecycle()
                    val personalAccounts by viewModel.personalAccountsList.collectAsStateWithLifecycle()

                    Scaffold(
                        topBar = {
                            Column {
                                HeaderSection(
                                    onOpenAiAssistant = { viewModel.setAiAssistantOpen(true) }
                                )
                                NavigationTabs(
                                    activeTab = activeTab,
                                    onTabSelected = { viewModel.setActiveTab(it) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (activeTab) {
                                "dashboard" -> DashboardView(
                                    dailyIncome = dailyIncome,
                                    dentalLabs = dentalLabs,
                                    dentalSupplies = dentalSupplies,
                                    expenses = expenses,
                                    patientDebts = patientDebts,
                                    personalAccounts = personalAccounts,
                                    onNavigateTab = { viewModel.setActiveTab(it) }
                                )

                                "daily-income" -> DailyIncomeSection(
                                    items = dailyIncome,
                                    onAddItem = { viewModel.addDailyIncome(it) },
                                    onUpdateItem = { viewModel.updateDailyIncome(it) },
                                    onDeleteItem = { viewModel.deleteDailyIncome(it) }
                                )

                                "labs" -> DentalLabsSection(
                                    items = dentalLabs,
                                    onAddItem = { viewModel.addDentalLab(it) },
                                    onUpdateItem = { viewModel.updateDentalLab(it) },
                                    onDeleteItem = { viewModel.deleteDentalLab(it) }
                                )

                                "supplies" -> DentalSuppliesSection(
                                    items = dentalSupplies,
                                    onAddItem = { viewModel.addDentalSupplier(it) },
                                    onUpdateItem = { viewModel.updateDentalSupplier(it) },
                                    onDeleteItem = { viewModel.deleteDentalSupplier(it) }
                                )

                                "expenses" -> ExpensesSection(
                                    items = expenses,
                                    onAddItem = { viewModel.addExpense(it) },
                                    onUpdateItem = { viewModel.updateExpense(it) },
                                    onDeleteItem = { viewModel.deleteExpense(it) }
                                )

                                "debts" -> PatientDebtsSection(
                                    items = patientDebts,
                                    onAddItem = { viewModel.addPatientDebt(it) },
                                    onUpdateItem = { viewModel.updatePatientDebt(it) },
                                    onDeleteItem = { viewModel.deletePatientDebt(it) }
                                )

                                "personal-accounts" -> PersonalAccountsSection(
                                    items = personalAccounts,
                                    onAddItem = { viewModel.addPersonalAccount(it) },
                                    onUpdateItem = { viewModel.updatePersonalAccount(it) },
                                    onDeleteItem = { viewModel.deletePersonalAccount(it) }
                                )

                                "general-audit" -> GeneralAuditSection(
                                    dailyIncome = dailyIncome,
                                    expenses = expenses,
                                    dentalLabs = dentalLabs,
                                    dentalSupplies = dentalSupplies,
                                    patientDebts = patientDebts,
                                    personalAccounts = personalAccounts
                                )
                            }
                        }

                        if (isAiOpen) {
                            AiAssistantDialog(
                                messages = aiMessages,
                                isThinking = isAiThinking,
                                onSendMessage = { viewModel.sendAiPrompt(it) },
                                onDismiss = { viewModel.setAiAssistantOpen(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
