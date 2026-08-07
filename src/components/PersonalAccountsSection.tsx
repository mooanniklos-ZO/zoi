import React, { useState } from 'react';
import { PersonalAccountItem } from '../types';
import { Plus, Search, Edit2, Trash2, X, Check } from 'lucide-react';

interface PersonalAccountsSectionProps {
  items: PersonalAccountItem[];
  onAddItem: (item: PersonalAccountItem) => void;
  onUpdateItem: (item: PersonalAccountItem) => void;
  onDeleteItem: (id: string) => void;
}

export const PersonalAccountsSection: React.FC<PersonalAccountsSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<PersonalAccountItem | null>(null);

  const [procedureName, setProcedureName] = useState('');
  const [totalAmount, setTotalAmount] = useState('');
  const [creditForHim, setCreditForHim] = useState('');
  const [debitOnHim, setDebitOnHim] = useState('');
  const [phone, setPhone] = useState('');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const filteredItems = items.filter(item =>
    item.procedureName.includes(searchQuery) ||
    item.notes.includes(searchQuery)
  );

  const handleOpenModal = (item?: PersonalAccountItem) => {
    if (item) {
      setEditingItem(item);
      setProcedureName(item.procedureName);
      setTotalAmount(item.totalAmount.toString());
      setCreditForHim(item.creditForHim.toString());
      setDebitOnHim(item.debitOnHim.toString());
      setPhone(item.phone);
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setProcedureName('');
      setTotalAmount('');
      setCreditForHim('');
      setDebitOnHim('');
      setPhone('');
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const tot = parseFloat(totalAmount) || 0;
    const credit = parseFloat(creditForHim) || 0;
    const debit = parseFloat(debitOnHim) || 0;
    const rem = Math.max(0, tot - credit + debit);

    const newItem: PersonalAccountItem = {
      id: editingItem ? editingItem.id : `pers-${Date.now()}`,
      procedureName: procedureName || 'عملية حسابية',
      totalAmount: tot,
      creditForHim: credit,
      debitOnHim: debit,
      remainingAmount: rem,
      date: editingItem ? editingItem.date : new Date().toISOString().split('T')[0],
      phone,
      autoMessageText: '',
      scheduledSendTime: new Date().toISOString().split('T')[0],
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
            placeholder="بحث باسم الإجراء أو القيد الحسابي..."
            className="w-full pr-10 pl-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:border-blue-600 font-medium"
          />
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>إضافة قيد جديد</span>
        </button>
      </div>

      <div className="space-y-3">
        {filteredItems.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد قيود حسابات شخصية مسجلة.
          </div>
        ) : (
          filteredItems.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                <h3 className="font-bold text-slate-900 text-lg">{item.procedureName}</h3>
                <div className="flex items-center gap-2">
                  <button onClick={() => handleOpenModal(item)} className="p-2 text-slate-500 hover:text-blue-600 rounded-lg hover:bg-slate-100">
                    <Edit2 className="w-4 h-4" />
                  </button>
                  <button onClick={() => onDeleteItem(item.id)} className="p-2 text-slate-500 hover:text-rose-600 rounded-lg hover:bg-slate-100">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div className="grid grid-cols-4 gap-2 bg-slate-100/70 p-3 rounded-xl text-center text-xs sm:text-sm">
                <div>
                  <span className="block text-slate-500 text-xs">الإجمالي</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.totalAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">له (دائن)</span>
                  <span className="font-bold text-emerald-600">{formatMoney(item.creditForHim)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">عليه (مدين)</span>
                  <span className="font-bold text-rose-600">{formatMoney(item.debitOnHim)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المتبقي</span>
                  <span className="font-bold text-slate-900">{formatMoney(item.remainingAmount)} ريال</span>
                </div>
              </div>

              {item.notes && <p className="text-xs text-slate-500">ملاحظات: {item.notes}</p>}
            </div>
          ))
        )}
      </div>

      {isModalOpen && (
        <div className="fixed inset-0 bg-slate-900/50 backdrop-blur-sm z-50 flex items-center justify-center p-4">
          <div className="bg-white rounded-3xl max-w-lg w-full p-6 space-y-4 shadow-2xl max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <h3 className="font-bold text-slate-900 text-lg">
                {editingItem ? 'تعديل القيد الحسابي' : 'إضافة قيد حسابي جديد'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-3 text-sm">
              <div>
                <label className="block font-bold text-slate-700 mb-1">اسم الإجراء أو العملية</label>
                <input
                  type="text"
                  required
                  value={procedureName}
                  onChange={(e) => setProcedureName(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

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

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">له (دائن)</label>
                  <input
                    type="number"
                    value={creditForHim}
                    onChange={(e) => setCreditForHim(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">عليه (مدين)</label>
                  <input
                    type="number"
                    value={debitOnHim}
                    onChange={(e) => setDebitOnHim(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">رقم الهاتف</label>
                <input
                  type="text"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">ملاحظات</label>
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
                  <span>حفظ القيد</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
