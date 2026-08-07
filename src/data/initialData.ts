import { 
  DailyIncomeItem, 
  DentalLabItem, 
  DentalSupplierItem, 
  ExpenseItem, 
  PatientDebtItem, 
  PersonalAccountItem 
} from '../types';

export const INITIAL_DAILY_INCOME: DailyIncomeItem[] = [
  {
    id: 'inc-1',
    patientName: 'أحمد علي حسن',
    caseType: 'حشو عصب',
    totalAmount: 150000,
    paidAmount: 100000,
    remainingAmount: 50000,
    diagnosis: 'تسوس عميق في الضرس السفلي الأيسر (36) مع التهاب العصب',
    treatment: 'تنظيف قنوات العصب والجلسة الأولى لضماد الكالسيوم',
    phone: '771234567',
    notes: 'موعد الجلسة القادمة يوم الثلاثاء',
    date: new Date().toISOString().split('T')[0]
  },
  {
    id: 'inc-2',
    patientName: 'فاطمة محمد الناصري',
    caseType: 'تركيبات زيركون',
    totalAmount: 300000,
    paidAmount: 200000,
    remainingAmount: 100000,
    diagnosis: 'فقدان الضرسين 14 و 15 بحاجة لجسر زيركوني',
    treatment: 'برد الأسنان وأخذ الطبعة التمهيدية وإرسالها للمعمل',
    phone: '772345678',
    notes: 'تم إرسال الطبعة لمعمل الأمل',
    date: new Date().toISOString().split('T')[0]
  },
  {
    id: 'inc-3',
    patientName: 'سالم عبدالله عمر',
    caseType: 'قلع جراحي',
    totalAmount: 80000,
    paidAmount: 80000,
    remainingAmount: 0,
    diagnosis: 'ضرس عقل مدفون أسفل الجانب الأيمن',
    treatment: 'قلع جراحي تحت التخدير الموضعي وتخييط الجرح',
    phone: '773456789',
    notes: 'تم صرف المضاد الحيوي والمسكن وصرف تعليمات ما بعد القلع',
    date: new Date().toISOString().split('T')[0]
  }
];

export const INITIAL_DENTAL_LABS: DentalLabItem[] = [
  {
    id: 'lab-1',
    labName: 'معمل الأمل لتركيبات الأسنان',
    workType: 'جسر زيركون (3 وحدات)',
    teethCount: 3,
    totalAmount: 90000,
    paidAmount: 50000,
    remainingAmount: 40000,
    sendDate: new Date().toISOString().split('T')[0],
    receiveDate: new Date(Date.now() + 86400000 * 4).toISOString().split('T')[0],
    patientName: 'فاطمة محمد الناصري',
    patientPhone: '772345678',
    labPhone: '770112233',
    notes: 'اللون A2 مع شفافية طبيعية عند الأطراف',
    status: 'قيد التصنيع'
  }
];

export const INITIAL_DENTAL_SUPPLIERS: DentalSupplierItem[] = [
  {
    id: 'sup-1',
    storeName: 'شركة السعيد لمستلزمات طب الأسنان',
    materialsType: 'بنج موضع إسباني + إبر + معجون حشوات عصب Apex',
    totalAmount: 180000,
    paidAmount: 100000,
    remainingAmount: 80000,
    purchaseDate: new Date().toISOString().split('T')[0],
    storePhone: '778899001',
    notes: 'المتبقي يرحل لنهاية الشهر'
  }
];

export const INITIAL_EXPENSES: ExpenseItem[] = [
  {
    id: 'exp-1',
    nurseExpense: 15000,
    homeKitchenExpense: 20000,
    familyExpense: 25000,
    generalExpense: 10000,
    fuelMaintenanceExpense: 15000,
    totalExpense: 85000,
    date: new Date().toISOString().split('T')[0],
    notes: 'خرجيات يومية وشراء وقود للمولد الكهربائي بالعيادة'
  }
];

export const INITIAL_PATIENT_DEBTS: PatientDebtItem[] = [
  {
    id: 'debt-1',
    patientName: 'مراد عبدالجليل الشميري',
    area: 'شارع جمال - تعز',
    guarantorName: 'د. خالد الحكيمي (معرف)',
    totalAmount: 220000,
    paidAmount: 120000,
    remainingAmount: 100000,
    date: new Date(Date.now() - 86400000 * 10).toISOString().split('T')[0],
    phone: '770001122',
    autoSmsText: 'عزيزي المريض مراد الشميري، نود تذكيركم بموعد سداد القسط المتبقي قدره 100,000 ريال لعيادة الدكتور مالك الرميمة. تحياتنا.',
    scheduledDate: new Date(Date.now() + 86400000 * 2).toISOString().split('T')[0],
    notes: 'المريض وعد بالسداد يوم الخميس القادم'
  }
];

export const INITIAL_PERSONAL_ACCOUNTS: PersonalAccountItem[] = [
  {
    id: 'pers-1',
    procedureName: 'صيانة الجهاز والمولد بالعيادة',
    totalAmount: 45000,
    creditForHim: 0,
    debitOnHim: 45000,
    remainingAmount: 45000,
    date: new Date().toISOString().split('T')[0],
    phone: '775544332',
    autoMessageText: 'تم تسجيل رسوم صيانة المولد في حساب العيادة.',
    scheduledSendTime: new Date().toISOString().split('T')[0],
    notes: 'دفع مهندس الصيانة لقطع غيار المولد'
  }
];
