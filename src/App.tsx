import React, { useState, useEffect } from 'react';
import { 
  DailyIncomeItem, 
  DentalLabItem, 
  DentalSupplierItem, 
  ExpenseItem, 
  PatientDebtItem, 
  PersonalAccountItem,
  ChatMessage 
} from './types';
import { 
  INITIAL_DAILY_INCOME, 
  INITIAL_DENTAL_LABS, 
  INITIAL_DENTAL_SUPPLIERS, 
  INITIAL_EXPENSES, 
  INITIAL_PATIENT_DEBTS, 
  INITIAL_PERSONAL_ACCOUNTS 
} from './data/initialData';

import { Header } from './components/Header';
import { DashboardView } from './components/DashboardView';
import { DailyIncomeSection } from './components/DailyIncomeSection';
import { DentalLabsSection } from './components/DentalLabsSection';
import { DentalSuppliesSection } from './components/DentalSuppliesSection';
import { ExpensesSection } from './components/ExpensesSection';
import { PatientDebtsSection } from './components/PatientDebtsSection';
import { PersonalAccountsSection } from './components/PersonalAccountsSection';
import { GeneralAuditSection } from './components/GeneralAuditSection';
import { QuickAccessModal } from './components/QuickAccessModal';

export function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [isAiOpen, setIsAiOpen] = useState(false);
  const [isAiThinking, setIsAiThinking] = useState(false);

  // Persistent States
  const [dailyIncome, setDailyIncome] = useState<DailyIncomeItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_income');
    return saved ? JSON.parse(saved) : INITIAL_DAILY_INCOME;
  });

  const [dentalLabs, setDentalLabs] = useState<DentalLabItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_labs');
    return saved ? JSON.parse(saved) : INITIAL_DENTAL_LABS;
  });

  const [dentalSupplies, setDentalSupplies] = useState<DentalSupplierItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_supplies');
    return saved ? JSON.parse(saved) : INITIAL_DENTAL_SUPPLIERS;
  });

  const [expenses, setExpenses] = useState<ExpenseItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_expenses');
    return saved ? JSON.parse(saved) : INITIAL_EXPENSES;
  });

  const [patientDebts, setPatientDebts] = useState<PatientDebtItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_debts');
    return saved ? JSON.parse(saved) : INITIAL_PATIENT_DEBTS;
  });

  const [personalAccounts, setPersonalAccounts] = useState<PersonalAccountItem[]>(() => {
    const saved = localStorage.getItem('dr_malik_personal');
    return saved ? JSON.parse(saved) : INITIAL_PERSONAL_ACCOUNTS;
  });

  const [aiMessages, setAiMessages] = useState<ChatMessage[]>([
    {
      id: '1',
      sender: 'assistant',
      text: 'مرحباً بك يا دكتور مالك الرميمة. أنا مساعدك المالي الذكي لعيادة الأسنان. كيف يمكنني مساعدتك اليوم؟',
      timestamp: new Date().toLocaleTimeString('ar-YE', { hour: '2-digit', minute: '2-digit' })
    }
  ]);

  // Sync to local storage
  useEffect(() => { localStorage.setItem('dr_malik_income', JSON.stringify(dailyIncome)); }, [dailyIncome]);
  useEffect(() => { localStorage.setItem('dr_malik_labs', JSON.stringify(dentalLabs)); }, [dentalLabs]);
  useEffect(() => { localStorage.setItem('dr_malik_supplies', JSON.stringify(dentalSupplies)); }, [dentalSupplies]);
  useEffect(() => { localStorage.setItem('dr_malik_expenses', JSON.stringify(expenses)); }, [expenses]);
  useEffect(() => { localStorage.setItem('dr_malik_debts', JSON.stringify(patientDebts)); }, [patientDebts]);
  useEffect(() => { localStorage.setItem('dr_malik_personal', JSON.stringify(personalAccounts)); }, [personalAccounts]);

  // AI assistant response logic
  const handleSendAiPrompt = (prompt: string) => {
    const userMsg: ChatMessage = {
      id: `user-${Date.now()}`,
      sender: 'user',
      text: prompt,
      timestamp: new Date().toLocaleTimeString('ar-YE', { hour: '2-digit', minute: '2-digit' })
    };
    setAiMessages(prev => [...prev, userMsg]);
    setIsAiThinking(true);

    setTimeout(() => {
      const formatMoney = (v: number) => v.toLocaleString('en-US');
      const incPaid = dailyIncome.reduce((acc, i) => acc + i.paidAmount, 0);
      const expTot = expenses.reduce((acc, i) => acc + i.totalExpense, 0);
      const labPaid = dentalLabs.reduce((acc, i) => acc + i.paidAmount, 0);
      const supPaid = dentalSupplies.reduce((acc, i) => acc + i.paidAmount, 0);
      const debtsTot = patientDebts.reduce((acc, i) => acc + i.remainingAmount, 0);
      const net = incPaid - (expTot + labPaid + supPaid);

      let responseText = '';
      if (prompt.includes('تقرير') || prompt.includes('جرد')) {
        responseText = `📊 التقرير المالي المباشر لعيادة د. مالك الرميمة:\n• إجمالي الإيرادات المقبوضة: ${formatMoney(incPaid)} ريال\n• مصاريف المعامل والمواد: ${formatMoney(labPaid + supPaid)} ريال\n• الخرجيات التشغيلية: ${formatMoney(expTot)} ريال\n• صافي الأرباح بالخزينة: ${formatMoney(net)} ريال\n• ديون المرضى المتبقية: ${formatMoney(debtsTot)} ريال.`;
      } else if (prompt.includes('ديون') || prompt.includes('مرضى')) {
        responseText = `بناءً على السجلات الحالية، يوجد إجمالي ديون مستحقة لدى المرضى قدرها ${formatMoney(debtsTot)} ريال يمني. يمكنك الانتقال إلى قسم (أقساط المرضى) لإرسال التذكيرات التلقائية عبر الواتساب.`;
      } else if (prompt.includes('مصاريف') || prompt.includes('خرجيات')) {
        responseText = `إجمالي الخرجيات والمصاريف التشغيلية المسجلة بالعيادة هو ${formatMoney(expTot)} ريال يمني (تشمل الممرضة، البيت، النثريات، وصيانة المولد).`;
      } else {
        responseText = `أهلاً دكتور مالك. السيولة النقدية الصافية المتبقية بالخزينة حالياً هي ${formatMoney(net)} ريال يمني، وإجمالي المقبوضات النقدية هو ${formatMoney(incPaid)} ريال. هل تود استخراج تفاصيل أخرى؟`;
      }

      const botMsg: ChatMessage = {
        id: `bot-${Date.now()}`,
        sender: 'assistant',
        text: responseText,
        timestamp: new Date().toLocaleTimeString('ar-YE', { hour: '2-digit', minute: '2-digit' })
      };
      setAiMessages(prev => [...prev, botMsg]);
      setIsAiThinking(false);
    }, 1000);
  };

  return (
    <div className="min-h-screen bg-slate-100 flex flex-col font-sans">
      <Header 
        activeTab={activeTab} 
        setActiveTab={setActiveTab} 
        onOpenAiAssistant={() => setIsAiOpen(true)} 
      />

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
        {activeTab === 'dashboard' && (
          <DashboardView 
            dailyIncome={dailyIncome}
            dentalLabs={dentalLabs}
            dentalSupplies={dentalSupplies}
            expenses={expenses}
            patientDebts={patientDebts}
            personalAccounts={personalAccounts}
            onNavigateTab={setActiveTab}
          />
        )}

        {activeTab === 'daily-income' && (
          <DailyIncomeSection 
            items={dailyIncome}
            onAddItem={(item) => setDailyIncome([item, ...dailyIncome])}
            onUpdateItem={(item) => setDailyIncome(dailyIncome.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setDailyIncome(dailyIncome.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'labs' && (
          <DentalLabsSection 
            items={dentalLabs}
            onAddItem={(item) => setDentalLabs([item, ...dentalLabs])}
            onUpdateItem={(item) => setDentalLabs(dentalLabs.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setDentalLabs(dentalLabs.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'supplies' && (
          <DentalSuppliesSection 
            items={dentalSupplies}
            onAddItem={(item) => setDentalSupplies([item, ...dentalSupplies])}
            onUpdateItem={(item) => setDentalSupplies(dentalSupplies.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setDentalSupplies(dentalSupplies.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'expenses' && (
          <ExpensesSection 
            items={expenses}
            onAddItem={(item) => setExpenses([item, ...expenses])}
            onUpdateItem={(item) => setExpenses(expenses.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setExpenses(expenses.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'debts' && (
          <PatientDebtsSection 
            items={patientDebts}
            onAddItem={(item) => setPatientDebts([item, ...patientDebts])}
            onUpdateItem={(item) => setPatientDebts(patientDebts.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setPatientDebts(patientDebts.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'personal-accounts' && (
          <PersonalAccountsSection 
            items={personalAccounts}
            onAddItem={(item) => setPersonalAccounts([item, ...personalAccounts])}
            onUpdateItem={(item) => setPersonalAccounts(personalAccounts.map(i => i.id === item.id ? item : i))}
            onDeleteItem={(id) => setPersonalAccounts(personalAccounts.filter(i => i.id !== id))}
          />
        )}

        {activeTab === 'general-audit' && (
          <GeneralAuditSection 
            dailyIncome={dailyIncome}
            expenses={expenses}
            dentalLabs={dentalLabs}
            dentalSupplies={dentalSupplies}
            patientDebts={patientDebts}
            personalAccounts={personalAccounts}
          />
        )}
      </main>

      <QuickAccessModal 
        isOpen={isAiOpen}
        onClose={() => setIsAiOpen(false)}
        messages={aiMessages}
        isThinking={isAiThinking}
        onSendMessage={handleSendAiPrompt}
      />
    </div>
  );
}

export default App;
