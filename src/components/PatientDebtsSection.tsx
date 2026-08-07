import React, { useState } from 'react';
import { PatientDebtItem } from '../types';
import { Plus, Search, MessageSquare, Phone, Edit2, Trash2, X, Check } from 'lucide-react';

interface PatientDebtsSectionProps {
  items: PatientDebtItem[];
  onAddItem: (item: PatientDebtItem) => void;
  onUpdateItem: (item: PatientDebtItem) => void;
  onDeleteItem: (id: string) => void;
}

export const PatientDebtsSection: React.FC<PatientDebtsSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<PatientDebtItem | null>(null);

  const [patientName, setPatientName] = useState('');
  const [area, setArea] = useState('');
  const [guarantorName, setGuarantorName] = useState('');
  const [totalAmount, setTotalAmount] = useState('');
  const [paidAmount, setPaidAmount] = useState('');
  const [phone, setPhone] = useState('');
  const [autoSmsText, setAutoSmsText] = useState('');
  const [scheduledDate, setScheduledDate] = useState('');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const filteredItems = items.filter(item =>
    item.patientName.includes(searchQuery) ||
    item.area.includes(searchQuery) ||
    item.guarantorName.includes(searchQuery) ||
    item.phone.includes(searchQuery)
  );

  const handleOpenModal = (item?: PatientDebtItem) => {
    if (item) {
      setEditingItem(item);
      setPatientName(item.patientName);
      setArea(item.area);
      setGuarantorName(item.guarantorName);
      setTotalAmount(item.totalAmount.toString());
      setPaidAmount(item.paidAmount.toString());
      setPhone(item.phone);
      setAutoSmsText(item.autoSmsText);
      setScheduledDate(item.scheduledDate);
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setPatientName('');
      setArea('');
      setGuarantorName('');
      setTotalAmount('');
      setPaidAmount('');
      setPhone('');
      setAutoSmsText('');
      setScheduledDate(new Date().toISOString().split('T')[0]);
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const tot = parseFloat(totalAmount) || 0;
    const paid = parseFloat(paidAmount) || 0;
    const rem = Math.max(0, tot - paid);

    const sms = autoSmsText || `عزيزي المريض ${patientName}، نود تذكيركم بموعد سداد القسط المتبقي قدره ${formatMoney(rem)} ريال لعيادة الدكتور مالك الرميمة. تحياتنا.`;

    const newItem: PatientDebtItem = {
      id: editingItem ? editingItem.id : `debt-${Date.now()}`,
      patientName: patientName || 'مريض',
      area,
      guarantorName,
      totalAmount: tot,
      paidAmount: paid,
      remainingAmount: rem,
      date: editingItem ? editingItem.date : new Date().toISOString().split('T')[0],
      phone,
      autoSmsText: sms,
      scheduledDate,
      notes
    };

    if (editingItem) {
      onUpdateItem(newItem);
    } else {
      onAddItem(newItem);
    }
    setIsModalOpen(false);
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
        <div className="relative flex-1">
          <Search className="w-5 h-5 text-slate-400 absolute right-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="بحث باسم المريض، المنطقة، الضامن، رقم الهاتف..."
            className="w-full pr-10 pl-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:border-blue-600 font-medium"
          />
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>تسجيل قسط / دين جديد</span>
        </button>
      </div>

      <div className="space-y-3">
        {filteredItems.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد ديون أو أقساط مرضى مسجلة.
          </div>
        ) : (
          filteredItems.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 border-b border-slate-100 pb-3">
                <div>
                  <h3 className="font-bold text-slate-900 text-lg">{item.patientName}</h3>
                  <p className="text-xs text-slate-500 font-medium">المنطقة: {item.area || '-'} | الضامن: {item.guarantorName || '-'}</p>
                </div>

                <div className="flex items-center gap-2">
                  <button onClick={() => handleOpenModal(item)} className="p-2 text-slate-500 hover:text-blue-600 rounded-lg hover:bg-slate-100">
                    <Edit2 className="w-4 h-4" />
                  </button>
                  <button onClick={() => onDeleteItem(item.id)} className="p-2 text-slate-500 hover:text-rose-600 rounded-lg hover:bg-slate-100">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div className="grid grid-cols-3 gap-2 bg-slate-100/70 p-3 rounded-xl text-center text-xs sm:text-sm">
                <div>
                  <span className="block text-slate-500 text-xs">إجمالي الدين</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.totalAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">الواصل</span>
                  <span className="font-bold text-emerald-600">{formatMoney(item.paidAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المتبقي كدين</span>
                  <span className="font-bold text-rose-600">{formatMoney(item.remainingAmount)} ريال</span>
                </div>
              </div>

              {item.autoSmsText && (
                <div className="bg-blue-50/70 p-3 rounded-xl border border-blue-100 text-xs text-blue-900 space-y-1">
                  <span className="font-bold block">صيغة التذكير التلقائية:</span>
                  <p className="italic">"{item.autoSmsText}"</p>
                </div>
              )}

              <div className="flex flex-col sm:flex-row sm:items-center justify-between text-xs gap-2 pt-1">
                <span className="text-slate-500">موعد الاستحقاق: {item.scheduledDate}</span>
                {item.phone && (
                  <div className="flex items-center gap-2">
                    <a href={`tel:${item.phone}`} className="flex items-center gap-1 text-slate-700 bg-slate-100 px-3 py-1.5 rounded-lg font-bold">
                      <Phone className="w-3.5 h-3.5" />
                      <span>اتصال</span>
                    </a>
                    <a
                      href={`https://api.whatsapp.com/send?phone=${item.phone}&text=${encodeURIComponent(item.autoSmsText)}`}
                      target="_blank"
                      rel="noreferrer"
                      className="flex items-center gap-1 text-white bg-emerald-600 hover:bg-emerald-700 px-3 py-1.5 rounded-lg font-bold shadow-sm"
                    >
                      <MessageSquare className="w-3.5 h-3.5" />
                      <span>تذكير واتساب</span>
                    </a>
                  </div>
                )}
              </div>
            </div>
          ))
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-lg w-full p-6 space-y-4 shadow-2xl max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <h3 className="font-bold text-slate-900 text-lg">
                {editingItem ? 'تعديل القسط والدين' : 'تسجيل قسط ودين مريض جديد'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-3 text-sm">
              <div>
                <label className="block font-bold text-slate-700 mb-1">اسم المريض</label>
                <input
                  type="text"
                  required
                  value={patientName}
                  onChange={(e) => setPatientName(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">المنطقة السكنية</label>
                  <input
                    type="text"
                    value={area}
                    onChange={(e) => setArea(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">اسم الضامن/المعرف</label>
                  <input
                    type="text"
                    value={guarantorName}
                    onChange={(e) => setGuarantorName(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">إجمالي المبلغ</label>
                  <input
                    type="number"
                    required
                    value={totalAmount}
                    onChange={(e) => setTotalAmount(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">المبلغ الواصل</label>
                  <input
                    type="number"
                    required
                    value={paidAmount}
                    onChange={(e) => setPaidAmount(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">رقم هاتف المريض</label>
                <input
                  type="text"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">نص التذكير التلقائي (رسالة)</label>
                <textarea
                  rows={2}
                  value={autoSmsText}
                  onChange={(e) => setAutoSmsText(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">تاريخ استحقاق القسط</label>
                <input
                  type="date"
                  value={scheduledDate}
                  onChange={(e) => setScheduledDate(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div className="flex items-center justify-end gap-3 pt-3">
                <button
                  type="button"
                  onClick={() => setIsModalOpen(false)}
                  className="px-5 py-2.5 bg-slate-100 text-slate-700 rounded-xl font-bold cursor-pointer"
                >
                  إلغاء
                </button>
                <button
                  type="submit"
                  className="px-5 py-2.5 bg-blue-900 hover:bg-blue-950 text-white rounded-xl font-bold cursor-pointer flex items-center gap-2"
                >
                  <Check className="w-4 h-4" />
                  <span>حفظ القسط</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
