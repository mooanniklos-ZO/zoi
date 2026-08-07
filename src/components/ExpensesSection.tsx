import React, { useState } from 'react';
import { ExpenseItem } from '../types';
import { Plus, Edit2, Trash2, X, Check } from 'lucide-react';

interface ExpensesSectionProps {
  items: ExpenseItem[];
  onAddItem: (item: ExpenseItem) => void;
  onUpdateItem: (item: ExpenseItem) => void;
  onDeleteItem: (id: string) => void;
}

export const ExpensesSection: React.FC<ExpensesSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<ExpenseItem | null>(null);

  const [nurse, setNurse] = useState('');
  const [home, setHome] = useState('');
  const [family, setFamily] = useState('');
  const [general, setGeneral] = useState('');
  const [fuel, setFuel] = useState('');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const grandTotal = items.reduce((acc, i) => acc + i.totalExpense, 0);

  const handleOpenModal = (item?: ExpenseItem) => {
    if (item) {
      setEditingItem(item);
      setNurse(item.nurseExpense.toString());
      setHome(item.homeKitchenExpense.toString());
      setFamily(item.familyExpense.toString());
      setGeneral(item.generalExpense.toString());
      setFuel(item.fuelMaintenanceExpense.toString());
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setNurse('');
      setHome('');
      setFamily('');
      setGeneral('');
      setFuel('');
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const n = parseFloat(nurse) || 0;
    const h = parseFloat(home) || 0;
    const f = parseFloat(family) || 0;
    const g = parseFloat(general) || 0;
    const fuelVal = parseFloat(fuel) || 0;
    const tot = n + h + f + g + fuelVal;

    const newItem: ExpenseItem = {
      id: editingItem ? editingItem.id : `exp-${Date.now()}`,
      nurseExpense: n,
      homeKitchenExpense: h,
      familyExpense: f,
      generalExpense: g,
      fuelMaintenanceExpense: fuelVal,
      totalExpense: tot,
      date: editingItem ? editingItem.date : new Date().toISOString().split('T')[0],
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
      
      {/* Total Banner */}
      <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <span className="text-xs font-bold text-slate-500 uppercase">إجمالي المصاريف والخرجيات المسجلة</span>
          <h2 className="text-3xl font-black text-rose-700">{formatMoney(grandTotal)} <span className="text-sm font-normal text-slate-500">ريال</span></h2>
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-rose-700 hover:bg-rose-800 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>تسجيل خرجيات جديدة</span>
        </button>
      </div>

      {/* List items */}
      <div className="space-y-3">
        {items.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد خرجيات مسجلة.
          </div>
        ) : (
          items.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                <span className="font-bold text-slate-800 text-sm">التاريخ: {item.date}</span>
                <div className="flex items-center gap-2">
                  <span className="font-black text-rose-700 text-base ml-2">المجموع: {formatMoney(item.totalExpense)} ريال</span>
                  <button onClick={() => handleOpenModal(item)} className="p-2 text-slate-500 hover:text-blue-600 rounded-lg hover:bg-slate-100">
                    <Edit2 className="w-4 h-4" />
                  </button>
                  <button onClick={() => onDeleteItem(item.id)} className="p-2 text-slate-500 hover:text-rose-600 rounded-lg hover:bg-slate-100">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>

              <div className="grid grid-cols-2 sm:grid-cols-5 gap-2 bg-slate-50 p-3 rounded-xl text-center text-xs">
                <div>
                  <span className="block text-slate-500">الممرضة</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.nurseExpense)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500">البيت والمطبخ</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.homeKitchenExpense)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500">الزوجة والأولاد</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.familyExpense)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500">نثرية العيادة</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.generalExpense)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500">صيانة والمولد</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.fuelMaintenanceExpense)} ريال</span>
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
                {editingItem ? 'تعديل الخرجيات' : 'تسجيل خرجيات جديدة'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-3 text-sm">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">الممرضة</label>
                  <input
                    type="number"
                    value={nurse}
                    onChange={(e) => setNurse(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">البيت والمطبخ</label>
                  <input
                    type="number"
                    value={home}
                    onChange={(e) => setHome(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">الزوجة والأولاد</label>
                  <input
                    type="number"
                    value={family}
                    onChange={(e) => setFamily(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">نثرية العيادة</label>
                  <input
                    type="number"
                    value={general}
                    onChange={(e) => setGeneral(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">بترول وصيانة المولد والكهرباء</label>
                <input
                  type="number"
                  value={fuel}
                  onChange={(e) => setFuel(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">ملاحظات / تفاصيل أخرى</label>
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
                  className="px-5 py-2.5 bg-rose-700 hover:bg-rose-800 text-white rounded-xl font-bold cursor-pointer flex items-center gap-2"
                >
                  <Check className="w-4 h-4" />
                  <span>حفظ الخرجيات</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
