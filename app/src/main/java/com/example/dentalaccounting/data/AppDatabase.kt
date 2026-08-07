package com.example.dentalaccounting.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        DailyIncomeEntity::class,
        DentalLabEntity::class,
        DentalSupplierEntity::class,
        ExpenseEntity::class,
        PatientDebtEntity::class,
        PersonalAccountEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun clinicDao(): ClinicDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dr_malik_dental_clinic.db"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.clinicDao())
                }
            }
        }

        suspend fun populateInitialData(dao: ClinicDao) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(Date())

            // Initial Daily Income
            dao.insertDailyIncome(
                DailyIncomeEntity(
                    patientName = "أحمد علي حسن",
                    caseType = "حشو عصب",
                    totalAmount = 150000.0,
                    paidAmount = 100000.0,
                    remainingAmount = 50000.0,
                    diagnosis = "تسوس عميق في الضرس السحلي الأيسر (36) مع التهاب العصب",
                    treatment = "تنظيف قنوات العصب والجلسة الأولى لضماد الكالسيوم",
                    phone = "771234567",
                    notes = "موعد الجلسة القادمة يوم الثلاثاء",
                    date = today
                )
            )
            dao.insertDailyIncome(
                DailyIncomeEntity(
                    patientName = "فاطمة محمد الناصري",
                    caseType = "تركيبات زيركون",
                    totalAmount = 300000.0,
                    paidAmount = 200000.0,
                    remainingAmount = 100000.0,
                    diagnosis = "فقدان الضرسين 14 و 15 بحاجة لجسر زيركوني",
                    treatment = "برد الأسنان وأخذ الطبعة التمهيدية وإرسالها للمعمل",
                    phone = "772345678",
                    notes = "تم إرسال الطبعة لمعمل الأمل",
                    date = today
                )
            )
            dao.insertDailyIncome(
                DailyIncomeEntity(
                    patientName = "سالم عبدالله عمر",
                    caseType = "قلع جراحي",
                    totalAmount = 80000.0,
                    paidAmount = 80000.0,
                    remainingAmount = 0.0,
                    diagnosis = "ضرس عقل مدفون أسفل الجانب الأيمن",
                    treatment = "قلع جراحي تحت التخدير الموضعي وتخييط الجرح",
                    phone = "773456789",
                    notes = "تم صرف المضاد الحيوي والمسكن وصرف تعليمات ما بعد القلع",
                    date = today
                )
            )

            // Initial Dental Labs
            dao.insertDentalLab(
                DentalLabEntity(
                    labName = "معمل الأمل لتركيبات الأسنان",
                    workType = "جسر زيركون (3 وحدات)",
                    teethCount = 3,
                    totalAmount = 90000.0,
                    paidAmount = 50000.0,
                    remainingAmount = 40000.0,
                    sendDate = today,
                    receiveDate = today,
                    patientName = "فاطمة محمد الناصري",
                    patientPhone = "772345678",
                    labPhone = "770112233",
                    notes = "اللون A2 مع شفافية طبيعية عند الأطراف",
                    status = "قيد التصنيع"
                )
            )

            // Initial Dental Suppliers
            dao.insertDentalSupplier(
                DentalSupplierEntity(
                    storeName = "شركة السعيد لمستلزمات طب الأسنان",
                    materialsType = "بنج موضع إسباني + إبر + معجون حشوات عصب Apex",
                    totalAmount = 180000.0,
                    paidAmount = 100000.0,
                    remainingAmount = 80000.0,
                    purchaseDate = today,
                    storePhone = "778899001",
                    notes = "المتبقي يرحل لنهاية الشهر"
                )
            )

            // Initial Expenses
            dao.insertExpense(
                ExpenseEntity(
                    nurseExpense = 15000.0,
                    homeKitchenExpense = 20000.0,
                    familyExpense = 25000.0,
                    generalExpense = 10000.0,
                    fuelMaintenanceExpense = 15000.0,
                    totalExpense = 85000.0,
                    date = today,
                    notes = "خرجيات يومية وشراء وقود للمولد الكهربائي بالعيادة"
                )
            )

            // Initial Patient Debts
            dao.insertPatientDebt(
                PatientDebtEntity(
                    patientName = "مراد عبدالجليل الشميري",
                    area = "شارع جمال - تعز",
                    guarantorName = "د. خالد الحكيمي (معرف)",
                    totalAmount = 220000.0,
                    paidAmount = 120000.0,
                    remainingAmount = 100000.0,
                    date = today,
                    phone = "770001122",
                    autoSmsText = "عزيزي المريض مراد الشميري، نود تذكيركم بموعد سداد القسط المتبقي قدره 100,000 ريال لعيادة الدكتور مالك الرميمة.",
                    scheduledDate = today,
                    notes = "المريض وعد بالسداد يوم الخميس القادم"
                )
            )

            // Initial Personal Account
            dao.insertPersonalAccount(
                PersonalAccountEntity(
                    procedureName = "صيانة الجهاز والمولد بالعيادة",
                    totalAmount = 45000.0,
                    creditForHim = 0.0,
                    debitOnHim = 45000.0,
                    remainingAmount = 45000.0,
                    date = today,
                    phone = "775544332",
                    autoMessageText = "تم تسجيل رسوم صيانة المولد في حساب العيادة.",
                    scheduledSendTime = today,
                    notes = "دفع مهندس الصيانة لقطع غيار المولد"
                )
            )
        }
    }
}
