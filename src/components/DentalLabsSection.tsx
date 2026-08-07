import React, { useState } from 'react';
import { DentalLabItem } from '../types';
import { Plus, Search, Phone, Edit2, Trash2, X, Check } from 'lucide-react';

interface DentalLabsSectionProps {
  items: DentalLabItem[];
  onAddItem: (item: DentalLabItem) => void;
  onUpdateItem: (item: DentalLabItem) => void;
  onDeleteItem: (id: string) => void;
}

export const DentalLabsSection: React.FC<DentalLabsSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('الكل');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<DentalLabItem | null>(null);

  const [labName, setLabName] = useState('');
  const [workType, setWorkType] = useState('');
  const [teethCount, setTeethCount] = useState('1');
  const [totalAmount, setTotalAmount] = useState('');
  const [paidAmount, setPaidAmount] = useState('');
  const [patientName, setPatientName] = useState('');
  const [labPhone, setLabPhone] = useState('');
  const [status, setStatus] = useState<'قيد التصنيع' | 'تم الاستلام' | 'متأخر'>('قيد التصنيع');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const filteredItems = items.filter(item => {
    const matchesFilter = statusFilter === 'الكل' || item.status === statusFilter;
    const matchesQuery = item.labName.includes(searchQuery) ||
      item.patientName.includes(searchQuery) ||
      item.workType.includes(searchQuery);
    return matchesFilter && matchesQuery;
  });

  const handleOpenModal = (item?: DentalLabItem) => {
    if (item) {
      setEditingItem(item);
      setLabName(item.labName);
      setWorkType(item.workType);
      setTeethCount(item.teethCount.toString());
      setTotalAmount(item.totalAmount.toString());
      setPaidAmount(item.paidAmount.toString());
      setPatientName(item.patientName);
      setLabPhone(item.labPhone);
      setStatus(item.status);
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setLabName('');
      setWorkType('');
      setTeethCount('1');
      setTotalAmount('');
      setPaidAmount('');
      setPatientName('');
      setLabPhone('');
      setStatus('قيد التصنيع');
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const tot = parseFloat(totalAmount) || 0;
    const paid = parseFloat(paidAmount) || 0;
    const rem = Math.max(0, tot - paid);

    const newItem: DentalLabItem = {
      id: editingItem ? editingItem.id : `lab-${Date.now()}`,
      labName: labName || 'معمل أسنان',
      workType: workType || 'تركيبة',
      teethCount: parseInt(teethCount) || 1,
      totalAmount: tot,
      paidAmount: paid,
      remainingAmount: rem,
      sendDate: editingItem ? editingItem.sendDate : new Date().toISOString().split('T')[0],
      receiveDate: editingItem ? editingItem.receiveDate : new Date(Date.now() + 86400000 * 4).toISOString().split('T')[0],
      patientName,
      patientPhone: '',
      labPhone,
      notes,
      status
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
            placeholder="بحث باسم المعمل، المريض، نوع التركيبة..."
            className="w-full pr-10 pl-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:border-blue-600 font-medium"
          />
        </div>

        {/* Filter Chips */}
        <div className="flex items-center gap-2 overflow-x-auto pb-1 sm:pb-0">
          {['الكل', 'قيد التصنيع', 'تم الاستلام', 'متأخر'].map((filter) => (
            <button
              key={filter}
              onClick={() => setStatusFilter(filter)}
              className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer whitespace-nowrap ${
                statusFilter === filter
                  ? 'bg-blue-900 text-white'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              }`}
            >
              {filter}
            </button>
          ))}
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>إضافة معمل جديد</span>
        </button>
      </div>

      {/* List items */}
      <div className="space-y-3">
        {filteredItems.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد معاملات معامل أسنان مسجلة.
          </div>
        ) : (
          filteredItems.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 border-b border-slate-100 pb-3">
                <div className="flex items-center gap-3">
                  <span className={`font-bold text-xs px-3 py-1 rounded-lg ${
                    item.status === 'تم الاستلام'
                      ? 'bg-emerald-100 text-emerald-800'
                      : item.status === 'متأخر'
                      ? 'bg-rose-100 text-rose-800'
                      : 'bg-amber-100 text-amber-800'
                  }`}>
                    {item.status}
                  </span>
                  <div>
                    <h3 className="font-bold text-slate-900 text-lg">{item.labName}</h3>
                    <p className="text-xs text-slate-500 font-medium">
                      {item.workType} ({item.teethCount} أسنان) - المريض: {item.patientName}
                    </p>
                  </div>
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

              {/* Financial Balance */}
              <div className="grid grid-cols-3 gap-2 bg-slate-100/70 p-3 rounded-xl text-center text-xs sm:text-sm">
                <div>
                  <span className="block text-slate-500 text-xs">إجمالي شغل المعمل</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.totalAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">الواصل للمعمل</span>
                  <span className="font-bold text-emerald-600">{formatMoney(item.paidAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المتبقي للمعمل</span>
                  <span className={`font-bold ${item.remainingAmount > 0 ? 'text-rose-600' : 'text-slate-700'}`}>
                    {formatMoney(item.remainingAmount)} ريال
                  </span>
                </div>
              </div>

              {/* Actions & Dates */}
              <div className="flex items-center justify-between text-xs pt-1">
                <span className="text-slate-500">الإرسال: {item.sendDate} | الاستلام المتوقع: {item.receiveDate}</span>
                {item.labPhone && (
                  <a href={`tel:${item.labPhone}`} className="flex items-center gap-1 text-blue-700 hover:underline font-bold">
                    <Phone className="w-3.5 h-3.5" />
                    <span>اتصال بالمعمل</span>
                  </a>
                )}
              </div>
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
                {editingItem ? 'تعديل معاملة معمل' : 'تسجيل معاملة معمل جديدة'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-4 text-sm">
              <div>
                <label className="block font-bold text-slate-700 mb-1">اسم المعمل</label>
                <input
                  type="text"
                  required
                  value={labName}
                  onChange={(e) => setLabName(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">نوع التركيبة / العمل</label>
                <input
                  type="text"
                  required
                  value={workType}
                  onChange={(e) => setWorkType(e.target.value)}
                  placeholder="جسر زيركون، طقم متحرك، سيراميك..."
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
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
                  <label className="block font-bold text-slate-700 mb-1">عدد الأسنان</label>
                  <input
                    type="number"
                    value={teethCount}
                    onChange={(e) => setTeethCount(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">إجمالي مبلغ المعمل</label>
                  <input
                    type="number"
                    required
                    value={totalAmount}
                    onChange={(e) => setTotalAmount(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">المبلغ المسدد للمعمل</label>
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
                <label className="block font-bold text-slate-700 mb-1">حالة الطلب</label>
                <select
                  value={status}
                  onChange={(e) => setStatus(e.target.value as any)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600 font-bold"
                >
                  <option value="قيد التصنيع">قيد التصنيع</option>
                  <option value="تم الاستلام">تم الاستلام</option>
                  <option value="متأخر">متأخر</option>
                </select>
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">رقم هاتف المعمل</label>
                <input
                  type="text"
                  value={labPhone}
                  onChange={(e) => setLabPhone(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">ملاحظات / درجة اللون</label>
                <textarea
                  rows={2}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
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
                  <span>حفظ البيانات</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
