import React, { useState } from 'react';
import { DailyIncomeItem } from '../types';
import { Plus, Search, Phone, MessageSquare, Edit2, Trash2, X, Check } from 'lucide-react';

interface DailyIncomeSectionProps {
  items: DailyIncomeItem[];
  onAddItem: (item: DailyIncomeItem) => void;
  onUpdateItem: (item: DailyIncomeItem) => void;
  onDeleteItem: (id: string) => void;
}

export const DailyIncomeSection: React.FC<DailyIncomeSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<DailyIncomeItem | null>(null);

  const [patientName, setPatientName] = useState('');
  const [caseType, setCaseType] = useState('حشو عصب');
  const [totalAmount, setTotalAmount] = useState('');
  const [paidAmount, setPaidAmount] = useState('');
  const [diagnosis, setDiagnosis] = useState('');
  const [treatment, setTreatment] = useState('');
  const [phone, setPhone] = useState('');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const filteredItems = items.filter(item =>
    item.patientName.includes(searchQuery) ||
    item.caseType.includes(searchQuery) ||
    item.phone.includes(searchQuery) ||
    item.diagnosis.includes(searchQuery)
  );

  const handleOpenModal = (item?: DailyIncomeItem) => {
    if (item) {
      setEditingItem(item);
      setPatientName(item.patientName);
      setCaseType(item.caseType);
      setTotalAmount(item.totalAmount.toString());
      setPaidAmount(item.paidAmount.toString());
      setDiagnosis(item.diagnosis);
      setTreatment(item.treatment);
      setPhone(item.phone);
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setPatientName('');
      setCaseType('حشو عصب');
      setTotalAmount('');
      setPaidAmount('');
      setDiagnosis('');
      setTreatment('');
      setPhone('');
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const tot = parseFloat(totalAmount) || 0;
    const paid = parseFloat(paidAmount) || 0;
    const rem = Math.max(0, tot - paid);

    const newItem: DailyIncomeItem = {
      id: editingItem ? editingItem.id : `inc-${Date.now()}`,
      patientName: patientName || 'مريض بدون اسم',
      caseType: caseType || 'علاج أسنان',
      totalAmount: tot,
      paidAmount: paid,
      remainingAmount: rem,
      diagnosis,
      treatment,
      phone,
      notes,
      date: editingItem ? editingItem.date : new Date().toISOString().split('T')[0]
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
      
      {/* Top Bar Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
        <div className="relative flex-1">
          <Search className="w-5 h-5 text-slate-400 absolute right-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="بحث باسم المريض، نوع الحالة، رقم الهاتف، التشخيص..."
            className="w-full pr-10 pl-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:border-blue-600 font-medium"
          />
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>تسجيل مريض جديد</span>
        </button>
      </div>

      {/* List items */}
      <div className="space-y-3">
        {filteredItems.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد سجلات دخل يومية مطابقة.
          </div>
        ) : (
          filteredItems.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 border-b border-slate-100 pb-3">
                <div className="flex items-center gap-3">
                  <span className="bg-blue-100 text-blue-900 font-bold text-xs px-3 py-1 rounded-lg">
                    {item.caseType}
                  </span>
                  <h3 className="font-bold text-slate-900 text-lg">{item.patientName}</h3>
                  <span className="text-xs text-slate-400 font-medium">{item.date}</span>
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

              {/* Treatment details */}
              {(item.diagnosis || item.treatment) && (
                <div className="text-xs text-slate-600 space-y-1 bg-slate-50 p-3 rounded-xl border border-slate-100">
                  {item.diagnosis && <p><span className="font-bold text-slate-800">التشخيص:</span> {item.diagnosis}</p>}
                  {item.treatment && <p><span className="font-bold text-slate-800">المعالجة:</span> {item.treatment}</p>}
                </div>
              )}

              {/* Balance Summary */}
              <div className="grid grid-cols-3 gap-2 bg-slate-100/70 p-3 rounded-xl text-center text-xs sm:text-sm">
                <div>
                  <span className="block text-slate-500 text-xs">الاتفاق الإجمالي</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.totalAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">الواصل للمحل</span>
                  <span className="font-bold text-emerald-600">{formatMoney(item.paidAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المتبقي</span>
                  <span className={`font-bold ${item.remainingAmount > 0 ? 'text-rose-600' : 'text-slate-700'}`}>
                    {formatMoney(item.remainingAmount)} ريال
                  </span>
                </div>
              </div>

              {/* Actions */}
              {item.phone && (
                <div className="flex items-center justify-between text-xs pt-1">
                  <span className="text-slate-500">الهاتف: {item.phone}</span>
                  <div className="flex items-center gap-2">
                    <a href={`tel:${item.phone}`} className="flex items-center gap-1 text-blue-700 hover:underline font-bold">
                      <Phone className="w-3.5 h-3.5" />
                      <span>اتصال</span>
                    </a>
                    <a
                      href={`https://api.whatsapp.com/send?phone=${item.phone}&text=${encodeURIComponent(`مرحباً ${item.patientName}، تذكير بموعد عيادة د. مالك الرميمة.`)}`}
                      target="_blank"
                      rel="noreferrer"
                      className="flex items-center gap-1 text-emerald-600 hover:underline font-bold"
                    >
                      <MessageSquare className="w-3.5 h-3.5" />
                      <span>واتساب</span>
                    </a>
                  </div>
                </div>
              )}
            </div>
          ))
        )}
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-lg w-full p-6 space-y-4 shadow-2xl max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <h3 className="font-bold text-slate-900 text-lg">
                {editingItem ? 'تعديل سجل الدخل' : 'تسجيل دخل ومريض جديد'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-4 text-sm">
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

              <div>
                <label className="block font-bold text-slate-700 mb-1">نوع الحالة العلاجية</label>
                <input
                  type="text"
                  required
                  value={caseType}
                  onChange={(e) => setCaseType(e.target.value)}
                  placeholder="حشو عصب، تركيبات، قلع جراحي، تبييض..."
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
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
                <label className="block font-bold text-slate-700 mb-1">التشخيص</label>
                <textarea
                  rows={2}
                  value={diagnosis}
                  onChange={(e) => setDiagnosis(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">المعالجة المنفذة</label>
                <textarea
                  rows={2}
                  value={treatment}
                  onChange={(e) => setTreatment(e.target.value)}
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
                  <span>حفظ السجل</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
