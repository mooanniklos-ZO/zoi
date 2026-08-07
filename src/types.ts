export interface DailyIncomeItem {
  id: string;
  patientName: string;
  caseType: string;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  diagnosis: string;
  treatment: string;
  phone: string;
  notes: string;
  date: string;
  receiptImage?: string;
}

export interface DentalLabItem {
  id: string;
  labName: string;
  workType: string;
  teethCount: number;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  sendDate: string;
  receiveDate: string;
  patientName: string;
  patientPhone: string;
  labPhone: string;
  notes: string;
  receiptImage?: string;
  status: 'قيد التصنيع' | 'تم الاستلام' | 'متأخر';
}

export interface DentalSupplierItem {
  id: string;
  storeName: string;
  materialsType: string;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  purchaseDate: string;
  storePhone: string;
  notes: string;
  receiptImage?: string;
}

export interface ExpenseItem {
  id: string;
  nurseExpense: number;
  homeKitchenExpense: number;
  familyExpense: number;
  generalExpense: number;
  fuelMaintenanceExpense: number;
  totalExpense: number;
  date: string;
  notes: string;
  receiptImage?: string;
}

export interface PatientDebtItem {
  id: string;
  patientName: string;
  area: string;
  guarantorName: string;
  totalAmount: number;
  paidAmount: number;
  remainingAmount: number;
  date: string;
  phone: string;
  autoSmsText: string;
  scheduledDate: string;
  notes: string;
  receiptImage?: string;
}

export interface PersonalAccountItem {
  id: string;
  procedureName: string;
  totalAmount: number;
  creditForHim: number;
  debitOnHim: number;
  remainingAmount: number;
  date: string;
  phone: string;
  autoMessageText: string;
  scheduledSendTime: string;
  notes: string;
  receiptImage?: string;
}

export interface ChatMessage {
  id: string;
  sender: 'user' | 'assistant';
  text: string;
  timestamp: string;
}
