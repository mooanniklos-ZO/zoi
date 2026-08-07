import React from 'react';
import { Stethoscope, Sparkles, Calendar, MapPin } from 'lucide-react';

interface HeaderProps {
  activeTab: string;
  setActiveTab: (tab: string) => void;
  onOpenAiAssistant: () => void;
}

export const Header: React.FC<HeaderProps> = ({
  activeTab,
  setActiveTab,
  onOpenAiAssistant
}) => {
  const currentDate = new Date().toLocaleDateString('ar-YE', {
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });

  return (
    <header className="bg-gradient-to-r from-blue-900 via-sky-800 to-teal-800 text-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
          
          {/* Clinic Title Branding */}
          <div className="flex items-center space-x-3 space-x-reverse">
            <div className="bg-white/20 p-3 rounded-2xl backdrop-blur-sm shadow-inner">
              <Stethoscope className="w-8 h-8 text-sky-200" />
            </div>
            <div>
              <span className="text-xs font-medium text-sky-200 block">النظام المحاسبي والمالي المتكامل</span>
              <h1 className="text-xl sm:text-2xl font-black text-white tracking-wide">
                عيادة الأسنان - د. مالك الرميمة
              </h1>
            </div>
          </div>

          {/* Quick AI & Info Toolbar */}
          <div className="flex items-center justify-between md:justify-end gap-3">
            <div className="hidden sm:flex items-center gap-2 bg-white/10 px-3 py-1.5 rounded-xl text-xs font-medium">
              <Calendar className="w-4 h-4 text-sky-300" />
              <span>{currentDate}</span>
              <span className="text-sky-300">•</span>
              <MapPin className="w-4 h-4 text-sky-300" />
              <span>تعز - اليمن</span>
            </div>

            <button
              onClick={onOpenAiAssistant}
              className="flex items-center gap-2 bg-gradient-to-r from-amber-400 to-amber-500 hover:from-amber-500 hover:to-amber-600 text-slate-900 px-4 py-2 rounded-xl font-bold text-sm shadow-lg hover:shadow-amber-500/20 transition-all cursor-pointer transform active:scale-95"
            >
              <Sparkles className="w-4 h-4" />
              <span>مساعد العيادة الذكي</span>
            </button>
          </div>
        </div>

        {/* Scrollable Navigation Tabs */}
        <nav className="mt-6 flex items-center space-x-2 space-x-reverse overflow-x-auto pb-2 scrollbar-none border-t border-white/10 pt-3">
          {[
            { id: 'dashboard', label: 'الرئيسية' },
            { id: 'daily-income', label: 'الدخل اليومي' },
            { id: 'labs', label: 'المعامل' },
            { id: 'supplies', label: 'المستلزمات' },
            { id: 'expenses', label: 'الخرجيات' },
            { id: 'debts', label: 'أقساط المرضى' },
            { id: 'personal-accounts', label: 'الحسابات الشخصية' },
            { id: 'general-audit', label: 'الجرد العام' }
          ].map((tab) => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-4 py-2 rounded-xl text-xs sm:text-sm font-bold whitespace-nowrap transition-all cursor-pointer ${
                activeTab === tab.id
                  ? 'bg-white text-blue-900 shadow-md'
                  : 'text-white/80 hover:bg-white/10 hover:text-white'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </nav>
      </div>
    </header>
  );
};
