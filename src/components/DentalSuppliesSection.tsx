import React, { useState } from 'react';
import { DentalSupplierItem } from '../types';
import { Plus, Search, Phone, Edit2, Trash2, X, Check } from 'lucide-react';

interface DentalSuppliesSectionProps {
  items: DentalSupplierItem[];
  onAddItem: (item: DentalSupplierItem) => void;
  onUpdateItem: (item: DentalSupplierItem) => void;
  onDeleteItem: (id: string) => void;
}

export const DentalSuppliesSection: React.FC<DentalSuppliesSectionProps> = ({
  items,
  onAddItem,
  onUpdateItem,
  onDeleteItem
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<DentalSupplierItem | null>(null);

  const [storeName, setStoreName] = useState('');
  const [materialsType, setMaterialsType] = useState('');
  const [totalAmount, setTotalAmount] = useState('');
  const [paidAmount, setPaidAmount] = useState('');
  const [storePhone, setStorePhone] = useState('');
  const [notes, setNotes] = useState('');

  const formatMoney = (val: number) => val.toLocaleString('en-US');

  const filteredItems = items.filter(item =>
    item.storeName.includes(searchQuery) ||
    item.materialsType.includes(searchQuery)
  );

  const handleOpenModal = (item?: DentalSupplierItem) => {
    if (item) {
      setEditingItem(item);
      setStoreName(item.storeName);
      setMaterialsType(item.materialsType);
      setTotalAmount(item.totalAmount.toString());
      setPaidAmount(item.paidAmount.toString());
      setStorePhone(item.storePhone);
      setNotes(item.notes);
    } else {
      setEditingItem(null);
      setStoreName('');
      setMaterialsType('');
      setTotalAmount('');
      setPaidAmount('');
      setStorePhone('');
      setNotes('');
    }
    setIsModalOpen(true);
  };

  const handleSave = (e: React.FormEvent) => {
    e.preventDefault();
    const tot = parseFloat(totalAmount) || 0;
    const paid = parseFloat(paidAmount) || 0;
    const rem = Math.max(0, tot - paid);

    const newItem: DentalSupplierItem = {
      id: editingItem ? editingItem.id : `sup-${Date.now()}`,
      storeName: storeName || 'محل مستلزمات',
      materialsType: materialsType || 'مواد أسنان',
      totalAmount: tot,
      paidAmount: paid,
      remainingAmount: rem,
      purchaseDate: editingItem ? editingItem.purchaseDate : new Date().toISOString().split('T')[0],
      storePhone,
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
            placeholder="بحث باسم الشركة أو المحل أو نوع المواد..."
            className="w-full pr-10 pl-4 py-2.5 bg-slate-50 border border-slate-200 rounded-xl text-sm focus:outline-none focus:border-blue-600 font-medium"
          />
        </div>

        <button
          onClick={() => handleOpenModal()}
          className="flex items-center justify-center gap-2 bg-blue-900 hover:bg-blue-950 text-white px-5 py-2.5 rounded-xl font-bold text-sm shadow-md transition-all cursor-pointer whitespace-nowrap"
        >
          <Plus className="w-4 h-4" />
          <span>تسجيل فاتورة جديدة</span>
        </button>
      </div>

      <div className="space-y-3">
        {filteredItems.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-2xl border border-slate-200 text-slate-500">
            لا توجد فواتير مستلزمات ومواد أسنان مسجلة.
          </div>
        ) : (
          filteredItems.map((item) => (
            <div key={item.id} className="bg-white p-5 rounded-2xl border border-slate-200/80 shadow-sm hover:shadow-md transition-all space-y-3">
              <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2 border-b border-slate-100 pb-3">
                <div>
                  <h3 className="font-bold text-slate-900 text-lg">{item.storeName}</h3>
                  <p className="text-xs text-slate-500 font-medium">{item.materialsType}</p>
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
                  <span className="block text-slate-500 text-xs">إجمالي الفاتورة</span>
                  <span className="font-bold text-slate-800">{formatMoney(item.totalAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المسدد للمحل</span>
                  <span className="font-bold text-emerald-600">{formatMoney(item.paidAmount)} ريال</span>
                </div>
                <div>
                  <span className="block text-slate-500 text-xs">المتبقي للمحل</span>
                  <span className={`font-bold ${item.remainingAmount > 0 ? 'text-rose-600' : 'text-slate-700'}`}>
                    {formatMoney(item.remainingAmount)} ريال
                  </span>
                </div>
              </div>

              <div className="flex items-center justify-between text-xs pt-1">
                <span className="text-slate-500">تاريخ الشراء: {item.purchaseDate}</span>
                {item.storePhone && (
                  <a href={`tel:${item.storePhone}`} className="flex items-center gap-1 text-blue-700 hover:underline font-bold">
                    <Phone className="w-3.5 h-3.5" />
                    <span>اتصال بالمحل</span>
                  </a>
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
                {editingItem ? 'تعديل فاتورة مستلزمات' : 'تسجيل فاتورة مستلزمات جديدة'}
              </h3>
              <button onClick={() => setIsModalOpen(false)} className="text-slate-400 hover:text-slate-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <form onSubmit={handleSave} className="space-y-4 text-sm">
              <div>
                <label className="block font-bold text-slate-700 mb-1">اسم الشركة أو المحل</label>
                <input
                  type="text"
                  required
                  value={storeName}
                  onChange={(e) => setStoreName(e.target.value)}
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div>
                <label className="block font-bold text-slate-700 mb-1">أنواع المواد المشتراة</label>
                <input
                  type="text"
                  required
                  value={materialsType}
                  onChange={(e) => setMaterialsType(e.target.value)}
                  placeholder="بنج، حشوات، كمامات، قفازات، مطهرات..."
                  className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block font-bold text-slate-700 mb-1">إجمالي الفاتورة</label>
                  <input
                    type="number"
                    required
                    value={totalAmount}
                    onChange={(e) => setTotalAmount(e.target.value)}
                    className="w-full p-2.5 bg-slate-50 border border-slate-200 rounded-xl focus:outline-none focus:border-blue-600"
                  />
                </div>
                <div>
                  <label className="block font-bold text-slate-700 mb-1">المبلغ المسدد للمحل</label>
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
                <label className="block font-bold text-slate-700 mb-1">رقم هاتف المحل</label>
                <input
                  type="text"
                  value={storePhone}
                  onChange={(e) => setStorePhone(e.target.value)}
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
                  <span>حفظ الفاتورة</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
