import React from 'react';
import { 
  DailyIncomeItem, 
  DentalLabItem, 
  DentalSupplierItem, 
  ExpenseItem, 
  PatientDebtItem, 
  PersonalAccountItem 
} from '../types';
import { 
  TrendingUp, 
  FlaskConical, 
  PackageCheck, 
  Receipt, 
  UserCheck, 
  Calculator,
  ArrowLeft,
  DollarSign,
  AlertCircle
} from 'lucide-react';

interface DashboardViewProps {
  dailyIncome: DailyIncomeItem[];
  dentalLabs: DentalLabItem[];
  dentalSupplies: DentalSupplierItem[];
  expenses: ExpenseItem[];
  patientDebts: PatientDebtItem[];
  personalAccounts: PersonalAccountItem[];
  onNavigateTab: (tab: string) => void;
}

export const DashboardView: React.FC<DashboardViewProps> = ({
  dailyIncome,
  dentalLabs,
  dentalSupplies,
  expenses,
  patientDebts,
  onNavigateTab
}) => {
  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const totalCashReceived = dailyIncome.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalLabPaid = dentalLabs.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalLabRemaining = dentalLabs.reduce((acc, i) => acc + i.remainingAmount, 0);
  const totalSupplierPaid = dentalSupplies.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalSupplierRemaining = dentalSupplies.reduce((acc, i) => acc + i.remainingAmount, 0);
  const totalExpenses = expenses.reduce((acc, i) => acc + i.totalExpense, 0);
  const totalPatientDebts = patientDebts.reduce((acc, i) => acc + i.remainingAmount, 0);

  const totalExpensesAll = totalLabPaid + totalSupplierPaid + totalExpenses;
  const netProfit = totalCashReceived - totalExpensesAll;

  return (
    <div className="space-y-6">
      
      {/* Banner Net Cash Card */}
      <div className={`p-6 sm:p-8 rounded-3xl text-white shadow-xl relative overflow-hidden transition-all ${
        netProfit >= 0 ? 'bg-gradient-to-r from-blue-900 via-sky-900 to-teal-800' : 'bg-gradient-to-r from-rose-900 via-red-800 to-rose-950'
      }`}>
        <div className="relative z-10 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-6">
          <div>
            <div className="flex items-center gap-2 text-sky-200 text-sm font-semibold mb-1">
              <DollarSign className="w-5 h-5" />
              <span>السيولة النقدية الصافية بالخزينة حالياً</span>
            </div>
            <h2 className="text-3xl sm:text-5xl font-black tracking-tight text-white">
              {formatMoney(netProfit)} <span className="text-xl sm:text-2xl font-normal text-sky-200">ريال يمني</span>
            </h2>
          </div>

          <div className="flex items-center gap-4 bg-white/10 p-4 rounded-2xl backdrop-blur-md">
            <div>
              <span className="block text-xs text-sky-200">إجمالي المقبوضات:</span>
              <span className="font-bold text-lg text-emerald-300">+{formatMoney(totalCashReceived)} ريال</span>
            </div>
            <div className="h-8 w-px bg-white/20"></div>
            <div>
              <span className="block text-xs text-sky-200">إجمالي المصاريف:</span>
              <span className="font-bold text-lg text-rose-300">-{formatMoney(totalExpensesAll)} ريال</span>
            </div>
          </div>
        </div>
      </div>

      {/* Grid Statistics */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        
        {/* Income Card */}
        <div 
          onClick={() => onNavigateTab('daily-income')}
          className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-slate-500 font-medium text-sm">الدخل اليومي (المقبوض)</span>
            <div className="bg-emerald-100 p-2.5 rounded-xl text-emerald-700 group-hover:scale-110 transition-transform">
              <TrendingUp className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black text-slate-800">{formatMoney(totalCashReceived)} <span className="text-xs font-normal text-slate-500">ريال</span></p>
          <span className="text-xs text-emerald-600 font-semibold mt-2 inline-flex items-center gap-1">
            عرض كشف المقبوضات
            <ArrowLeft className="w-3.5 h-3.5" />
          </span>
        </div>

        {/* Labs Card */}
        <div 
          onClick={() => onNavigateTab('labs')}
          className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-slate-500 font-medium text-sm">مصاريف المعامل المسددة</span>
            <div className="bg-sky-100 p-2.5 rounded-xl text-sky-700 group-hover:scale-110 transition-transform">
              <FlaskConical className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black text-slate-800">{formatMoney(totalLabPaid)} <span className="text-xs font-normal text-slate-500">ريال</span></p>
          <span className="text-xs text-rose-600 font-medium mt-2 block">متبقي للمعامل: {formatMoney(totalLabRemaining)} ريال</span>
        </div>

        {/* Supplies Card */}
        <div 
          onClick={() => onNavigateTab('supplies')}
          className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-slate-500 font-medium text-sm">مستلزمات الأسنان المسددة</span>
            <div className="bg-amber-100 p-2.5 rounded-xl text-amber-700 group-hover:scale-110 transition-transform">
              <PackageCheck className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black text-slate-800">{formatMoney(totalSupplierPaid)} <span className="text-xs font-normal text-slate-500">ريال</span></p>
          <span className="text-xs text-rose-600 font-medium mt-2 block">متبقي للموردين: {formatMoney(totalSupplierRemaining)} ريال</span>
        </div>

        {/* Expenses Card */}
        <div 
          onClick={() => onNavigateTab('expenses')}
          className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-slate-500 font-medium text-sm">خرجيات العيادة التشغيلية</span>
            <div className="bg-rose-100 p-2.5 rounded-xl text-rose-700 group-hover:scale-110 transition-transform">
              <Receipt className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black text-slate-800">{formatMoney(totalExpenses)} <span className="text-xs font-normal text-slate-500">ريال</span></p>
          <span className="text-xs text-slate-500 mt-2 block">الممرضة، المطبخ، النثريات، المولد</span>
        </div>

        {/* Debts Card */}
        <div 
          onClick={() => onNavigateTab('debts')}
          className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-slate-500 font-medium text-sm">ديون وأقساط المرضى المستحقة</span>
            <div className="bg-purple-100 p-2.5 rounded-xl text-purple-700 group-hover:scale-110 transition-transform">
              <UserCheck className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black text-purple-900">{formatMoney(totalPatientDebts)} <span className="text-xs font-normal text-slate-500">ريال</span></p>
          <span className="text-xs text-purple-700 font-medium mt-2 block">إرسال التذكيرات التلقائية</span>
        </div>

        {/* General Audit Card */}
        <div 
          onClick={() => onNavigateTab('general-audit')}
          className="bg-gradient-to-br from-teal-700 to-sky-900 text-white p-5 rounded-2xl shadow-md hover:shadow-lg transition-all cursor-pointer group"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-teal-100 font-medium text-sm">الجرد العام والتقرير المالي</span>
            <div className="bg-white/20 p-2.5 rounded-xl text-white group-hover:scale-110 transition-transform">
              <Calculator className="w-5 h-5" />
            </div>
          </div>
          <p className="text-2xl font-black">كشف حساب دوري</p>
          <span className="text-xs text-teal-200 mt-2 inline-flex items-center gap-1 font-bold">
            عرض وتصدير التقرير
            <ArrowLeft className="w-3.5 h-3.5" />
          </span>
        </div>
      </div>

      {/* Quick Action Alert Banner */}
      {totalPatientDebts > 0 && (
        <div className="bg-amber-50 border border-amber-200 p-4 rounded-2xl flex items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <AlertCircle className="w-6 h-6 text-amber-600 shrink-0" />
            <p className="text-sm font-bold text-amber-900">
              يوجد إجمالي ديون مستحقة لدى المرضى قدرها {formatMoney(totalPatientDebts)} ريال يمني.
            </p>
          </div>
          <button 
            onClick={() => onNavigateTab('debts')}
            className="px-4 py-2 bg-amber-600 hover:bg-amber-700 text-white font-bold text-xs rounded-xl transition-all whitespace-nowrap cursor-pointer"
          >
            تذكير المرضى
          </button>
        </div>
      )}
    </div>
  );
};
