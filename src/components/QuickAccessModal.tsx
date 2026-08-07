import React, { useState } from 'react';
import { ChatMessage } from '../types';
import { Sparkles, Send, X, Bot, User } from 'lucide-react';

interface QuickAccessModalProps {
  isOpen: boolean;
  onClose: () => void;
  messages: ChatMessage[];
  isThinking: boolean;
  onSendMessage: (text: string) => void;
}

export const QuickAccessModal: React.FC<QuickAccessModalProps> = ({
  isOpen,
  onClose,
  messages,
  isThinking,
  onSendMessage
}) => {
  const [inputText, setInputText] = useState('');

  if (!isOpen) return null;

  const handleSend = (e: React.FormEvent) => {
    e.preventDefault();
    if (inputText.trim()) {
      onSendMessage(inputText.trim());
      setInputText('');
    }
  };

  const suggestions = [
    "أريد تقرير الجرد المالي اليومي",
    "كم إجمالي ديون المرضى المستحقة؟",
    "كم إجمالي مصاريف العيادة والخرجيات؟"
  ];

  return (
    <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-3xl max-w-lg w-full h-[550px] flex flex-col shadow-2xl overflow-hidden">
        
        {/* Header */}
        <div className="bg-gradient-to-r from-blue-900 to-sky-800 text-white p-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-amber-300" />
            <h3 className="font-bold text-base">مساعد العيادة الذكي</h3>
          </div>
          <button onClick={onClose} className="p-1 hover:bg-white/20 rounded-lg transition-colors cursor-pointer">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Quick Prompts */}
        <div className="p-3 bg-slate-50 border-b border-slate-200 flex items-center gap-2 overflow-x-auto text-xs">
          {suggestions.map((sug, i) => (
            <button
              key={i}
              onClick={() => onSendMessage(sug)}
              className="bg-white hover:bg-blue-50 text-slate-700 hover:text-blue-900 border border-slate-200 px-3 py-1.5 rounded-full font-medium whitespace-nowrap cursor-pointer transition-colors shadow-2xs"
            >
              {sug}
            </button>
          ))}
        </div>

        {/* Chat History */}
        <div className="flex-1 p-4 overflow-y-auto space-y-3 bg-slate-100/50">
          {messages.map((msg) => {
            const isUser = msg.sender === 'user';
            return (
              <div key={msg.id} className={`flex items-start gap-2 ${isUser ? 'flex-row-reverse' : 'flex-row'}`}>
                <div className={`p-2 rounded-xl text-white ${isUser ? 'bg-blue-900' : 'bg-amber-500'}`}>
                  {isUser ? <User className="w-4 h-4" /> : <Bot className="w-4 h-4" />}
                </div>
                <div className={`max-w-[80%] p-3 rounded-2xl text-xs sm:text-sm leading-relaxed ${
                  isUser ? 'bg-blue-900 text-white rounded-tr-none' : 'bg-white text-slate-800 border border-slate-200 shadow-2xs rounded-tl-none'
                }`}>
                  <p>{msg.text}</p>
                  <span className={`block text-[10px] mt-1 ${isUser ? 'text-blue-200 text-left' : 'text-slate-400 text-right'}`}>
                    {msg.timestamp}
                  </span>
                </div>
              </div>
            );
          })}

          {isThinking && (
            <div className="flex items-center gap-2 text-xs text-blue-900 font-bold bg-white p-3 rounded-2xl border border-slate-200 w-fit">
              <Sparkles className="w-4 h-4 animate-spin text-amber-500" />
              <span>جاري تحليل أرقام العيادة المالية...</span>
            </div>
          )}
        </div>

        {/* Footer Input */}
        <form onSubmit={handleSend} className="p-3 bg-white border-t border-slate-200 flex items-center gap-2">
          <input
            type="text"
            value={inputText}
            onChange={(e) => setInputText(e.target.value)}
            placeholder="اسأل عن حسابات العيادة، الإيرادات، الديون..."
            className="flex-1 bg-slate-50 border border-slate-200 px-4 py-2.5 rounded-xl text-sm focus:outline-none focus:border-blue-600"
          />
          <button
            type="submit"
            className="p-2.5 bg-blue-900 hover:bg-blue-950 text-white rounded-xl cursor-pointer transition-colors"
          >
            <Send className="w-4 h-4" />
          </button>
        </form>
      </div>
    </div>
  );
};
