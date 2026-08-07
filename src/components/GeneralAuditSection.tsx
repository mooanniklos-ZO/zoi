import React, { useState } from 'react';
import { 
  DailyIncomeItem, 
  ExpenseItem, 
  DentalLabItem, 
  DentalSupplierItem, 
  PatientDebtItem, 
  PersonalAccountItem 
} from '../types';
import { Printer, FileText, CheckCircle2 } from 'lucide-react';

interface GeneralAuditSectionProps {
  dailyIncome: DailyIncomeItem[];
  expenses: ExpenseItem[];
  dentalLabs: DentalLabItem[];
  dentalSupplies: DentalSupplierItem[];
  patientDebts: PatientDebtItem[];
  personalAccounts: PersonalAccountItem[];
}

export const GeneralAuditSection: React.FC<GeneralAuditSectionProps> = ({
  dailyIncome,
  expenses,
  dentalLabs,
  dentalSupplies,
  patientDebts
}) => {
  const [showPrintModal, setShowPrintModal] = useState(false);

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const totalIncomePaid = dailyIncome.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalLabPaid = dentalLabs.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalLabRemaining = dentalLabs.reduce((acc, i) => acc + i.remainingAmount, 0);
  const totalSupplierPaid = dentalSupplies.reduce((acc, i) => acc + i.paidAmount, 0);
  const totalSupplierRemaining = dentalSupplies.reduce((acc, i) => acc + i.remainingAmount, 0);
  const totalExpenses = expenses.reduce((acc, i) => acc + i.totalExpense, 0);
  const totalPatientDebts = patientDebts.reduce((acc, i) => acc + i.remainingAmount, 0);

  const totalCostsPaid = totalLabPaid + totalSupplierPaid + totalExpenses;
  const netProfit = totalIncomePaid - totalCostsPaid;

  return (
    <div className="space-y-6">
      
      {/* Title Bar */}
      <div className="flex items-center justify-between bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
        <div>
          <h2 className="text-xl font-bold text-slate-900">تقرير الجرد العام والتدقيق المالي الدوري</h2>
          <p className="text-xs text-slate-500">كشف حساب الحركة النقدية الشاملة لعيادة د. مالك الرميمة</p>
        </div>

        <button
          onClick={() => setShowPrintModal(true)}
          className="flex items-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer"
        >
          <Printer className="w-4 h-4" />
          <span>معاينة وتصدير الكشف</span>
        </button>
      </div>

      {/* Main Statement Card */}
      <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-6">
        <div className="border-b border-slate-100 pb-4">
          <h3 className="font-bold text-slate-900 text-lg flex items-center gap-2">
            <FileText className="w-5 h-5 text-blue-900" />
            <span>الخلاصة المالية والميزانية المباشرة</span>
          </h3>
        </div>

        <div className="space-y-4 text-sm">
          <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50">
            <span className="font-semibold text-slate-700">إجمالي إيرادات المرضى المقبوضة نقدياً:</span>
            <span className="font-black text-emerald-600 text-base">+{formatMoney(totalIncomePaid)} ريال</span>
          </div>

          <div className="space-y-2 pr-4 border-r-2 border-slate-200">
            <div className="flex items-center justify-between py-1 text-slate-600">
              <span>إجمالي مدفوعات المعامل:</span>
              <span className="font-bold text-rose-600">-{formatMoney(totalLabPaid)} ريال</span>
            </div>
            <div className="flex items-center justify-between py-1 text-slate-600">
              <span>إجمالي مدفوعات المستلزمات والمواد:</span>
              <span className="font-bold text-rose-600">-{formatMoney(totalSupplierPaid)} ريال</span>
            </div>
            <div className="flex items-center justify-between py-1 text-slate-600">
              <span>إجمالي الخرجيات والمصاريف التشغيلية:</span>
              <span className="font-bold text-rose-600">-{formatMoney(totalExpenses)} ريال</span>
            </div>
          </div>

          <div className="flex items-center justify-between p-3 rounded-xl bg-rose-50 border border-rose-100">
            <span className="font-bold text-rose-900">مجموع كافة المصاريف والتكاليف المسددة:</span>
            <span className="font-black text-rose-700 text-base">-{formatMoney(totalCostsPaid)} ريال</span>
          </div>

          <div className={`p-5 rounded-2xl border flex items-center justify-between ${
            netProfit >= 0 ? 'bg-emerald-50 border-emerald-200 text-emerald-950' : 'bg-rose-50 border-rose-200 text-rose-950'
          }`}>
            <div>
              <span className="block text-xs font-bold text-slate-500 uppercase">صافي الأرباح النقدية المتبقية بالخزينة</span>
              <span className="text-2xl sm:text-3xl font-black">{formatMoney(netProfit)} ريال يمني</span>
            </div>
            <CheckCircle2 className={`w-8 h-8 ${netProfit >= 0 ? 'text-emerald-600' : 'text-rose-600'}`} />
          </div>
        </div>
      </div>

      {/* Liabilities Card */}
      <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
        <h3 className="font-bold text-slate-900 text-lg">الالتزامات والديون المعلقة</h3>
        
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3 text-center text-sm">
          <div className="p-4 rounded-2xl bg-purple-50 border border-purple-100">
            <span className="block text-xs font-bold text-purple-700">ديون المرضى المستحقة للعيادة</span>
            <span className="font-black text-purple-900 text-lg mt-1 block">{formatMoney(totalPatientDebts)} ريال</span>
          </div>
          <div className="p-4 rounded-2xl bg-amber-50 border border-amber-100">
            <span className="block text-xs font-bold text-amber-700">مستحقات غير مسددة للمعامل</span>
            <span className="font-black text-amber-900 text-lg mt-1 block">{formatMoney(totalLabRemaining)} ريال</span>
          </div>
          <div className="p-4 rounded-2xl bg-amber-50 border border-amber-100">
            <span className="block text-xs font-bold text-amber-700">مستحقات غير مسددة للموردين</span>
            <span className="font-black text-amber-900 text-lg mt-1 block">{formatMoney(totalSupplierRemaining)} ريال</span>
          </div>
        </div>
      </div>

      {/* Modal Preview */}
      {showPrintModal && (
        <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-lg w-full p-6 space-y-4 shadow-2xl">
            <div className="text-center space-y-1 border-b border-slate-100 pb-3">
              <h3 className="font-black text-blue-900 text-xl">عيادة الأسنان - د. مالك الرميمة</h3>
              <p className="text-xs text-slate-500">كشف الحساب المالي المباشر - {new Date().toLocaleDateString('ar-YE')}</p>
            </div>

            <div className="space-y-2 text-xs sm:text-sm text-slate-800 py-2">
              <div className="flex justify-between py-1 border-b">
                <span>الإيرادات النقدية المقبوضة:</span>
                <span className="font-bold text-emerald-600">+{formatMoney(totalIncomePaid)} ريال</span>
              </div>
              <div className="flex justify-between py-1 border-b">
                <span>مصاريف المعامل:</span>
                <span className="font-bold text-rose-600">-{formatMoney(totalLabPaid)} ريال</span>
              </div>
              <div className="flex justify-between py-1 border-b">
                <span>مصاريف المستلزمات:</span>
                <span className="font-bold text-rose-600">-{formatMoney(totalSupplierPaid)} ريال</span>
              </div>
              <div className="flex justify-between py-1 border-b">
                <span>الخرجيات التشغيلية:</span>
                <span className="font-bold text-rose-600">-{formatMoney(totalExpenses)} ريال</span>
              </div>
              <div className="flex justify-between py-2 font-black text-base bg-slate-100 p-2 rounded-xl">
                <span>صافي الأرباح النقدية بالخزينة:</span>
                <span className="text-blue-900">{formatMoney(netProfit)} ريال</span>
              </div>
            </div>

            <div className="flex items-center justify-end gap-3 pt-2">
              <button
                onClick={() => setShowPrintModal(false)}
                className="w-full py-2.5 bg-blue-900 text-white rounded-xl font-bold text-sm cursor-pointer"
              >
                إغلاق المعاينة
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
