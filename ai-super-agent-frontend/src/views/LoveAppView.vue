<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchLoveAppChat } from '../api/ai.js'
import { useSeo } from '../composables/useSeo.js'

useSeo({
  title: 'AI 恋爱大师',
  description: 'AI 恋爱大师，专业的 AI 情感咨询助手，帮你解决恋爱困惑、分析情感问题，提供贴心的恋爱建议和情感支持。',
})

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatContainer = ref(null)
const chatId = ref('')

onMounted(() => {
  chatId.value = generateChatId()
})

function generateChatId() {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substring(2, 9)
}

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

async function sendMessage() {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  messages.value.push({ role: 'user', content: message })
  inputMessage.value = ''
  isLoading.value = true

  const aiIndex = messages.value.length
  messages.value.push({ role: 'ai', content: '' })
  scrollToBottom()

  try {
    await fetchLoveAppChat(message, chatId.value, (chunk, done) => {
      if (done) {
        isLoading.value = false
        return
      }
      if (chunk) {
        messages.value[aiIndex].content += chunk
        scrollToBottom()
      }
    })
  } catch (error) {
    messages.value[aiIndex].content += '\n\n❌ 请求失败，请稍后重试'
    isLoading.value = false
  }
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

function goHome() {
  router.push('/')
}
</script>

<template>
  <div class="chat-page">
    <header class="chat-header">
      <button class="back-btn" @click="goHome" aria-label="返回首页">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 12H5M12 19l-7-7 7-7"/></svg>
      </button>
      <div class="header-center">
        <span class="header-icon">💕</span>
        <h1>AI 恋爱大师</h1>
      </div>
      <span class="chat-badge">#{{ chatId.slice(-8) }}</span>
    </header>

    <main class="chat-body" ref="chatContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">💕</div>
        <h2>你好，我是 AI 恋爱大师</h2>
        <p>有什么情感问题都可以和我聊聊，这里是你安全的树洞～</p>
      </div>

      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-row', msg.role === 'user' ? 'row-user' : 'row-ai']"
      >
        <div class="msg-avatar">{{ msg.role === 'user' ? '🧑' : '💕' }}</div>
        <div class="msg-body">
          <div class="msg-bubble">{{ msg.content }}</div>
        </div>
      </div>

      <div v-if="isLoading && messages[messages.length - 1]?.content === ''" class="message-row row-ai">
        <div class="msg-avatar">💕</div>
        <div class="msg-body">
          <div class="msg-bubble typing">
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
            <span class="typing-dot"></span>
          </div>
        </div>
      </div>
    </main>

    <footer class="chat-footer">
      <div class="input-box">
        <textarea
          v-model="inputMessage"
          @keydown="handleKeydown"
          placeholder="输入你的问题..."
          rows="1"
          :disabled="isLoading"
        ></textarea>
        <button
          class="send-btn"
          @click="sendMessage"
          :disabled="isLoading || !inputMessage.trim()"
          aria-label="发送消息"
        >
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
        </button>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.chat-page {
  height: 100vh;
  height: 100dvh;
  display: flex;
  flex-direction: column;
  background: #fef7f0;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  background: linear-gradient(135deg, #ff6b9d, #ff8a80);
  color: #fff;
  gap: 14px;
  flex-shrink: 0;
  box-shadow: 0 2px 12px rgba(255, 107, 157, 0.25);
}

.back-btn {
  background: rgba(255, 255, 255, 0.18);
  border: none;
  color: #fff;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  flex-shrink: 0;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header-center {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.header-icon {
  font-size: 1.3rem;
}

.chat-header h1 {
  font-size: 1.1rem;
  font-weight: 700;
}

.chat-badge {
  font-size: 0.7rem;
  opacity: 0.75;
  background: rgba(255, 255, 255, 0.18);
  padding: 4px 10px;
  border-radius: 20px;
  font-family: var(--font-mono);
  flex-shrink: 0;
}

.chat-body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  scroll-behavior: smooth;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  text-align: center;
  padding: 20px;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 16px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

.empty-state h2 {
  font-size: 1.2rem;
  color: var(--color-text);
  margin-bottom: 8px;
}

.empty-state p {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  max-width: 280px;
}

.message-row {
  display: flex;
  margin-bottom: 18px;
  gap: 10px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

.row-user {
  flex-direction: row-reverse;
}

.msg-avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.1rem;
  flex-shrink: 0;
  background: #fff;
  box-shadow: var(--shadow-sm);
}

.msg-body {
  max-width: 72%;
  min-width: 0;
}

.msg-bubble {
  padding: 11px 16px;
  border-radius: var(--radius-xl);
  line-height: 1.65;
  font-size: 0.92rem;
  white-space: pre-wrap;
  word-break: break-word;
}

.row-user .msg-bubble {
  background: linear-gradient(135deg, #ff6b9d, #ff8a80);
  color: #fff;
  border-bottom-right-radius: 6px;
}

.row-ai .msg-bubble {
  background: #fff;
  color: #333;
  border-bottom-left-radius: 6px;
  box-shadow: var(--shadow-sm);
}

.typing {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 14px 20px;
}

.typing-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #ccc;
  animation: dotPulse 1.4s infinite both;
}

.typing-dot:nth-child(2) { animation-delay: 0.2s; }
.typing-dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes dotPulse {
  0%, 80%, 100% { opacity: 0.3; transform: scale(0.8); }
  40% { opacity: 1; transform: scale(1); }
}

.chat-footer {
  padding: 14px 20px;
  background: #fff;
  border-top: 1px solid #f0e8e0;
  flex-shrink: 0;
}

.input-box {
  display: flex;
  gap: 10px;
  align-items: flex-end;
  max-width: 800px;
  margin: 0 auto;
}

.input-box textarea {
  flex: 1;
  padding: 11px 16px;
  border: 2px solid #f0e8e0;
  border-radius: var(--radius-xl);
  font-size: 0.92rem;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
  max-height: 120px;
  transition: border-color 0.2s;
  background: #fefaf7;
}

.input-box textarea:focus {
  border-color: #ff6b9d;
  background: #fff;
}

.send-btn {
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, #ff6b9d, #ff8a80);
  color: #fff;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.08);
  box-shadow: 0 4px 14px rgba(255, 107, 157, 0.4);
}

.send-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

@media (max-width: 640px) {
  .chat-header {
    padding: 12px 14px;
    gap: 10px;
  }

  .chat-header h1 {
    font-size: 1rem;
  }

  .chat-badge {
    display: none;
  }

  .chat-body {
    padding: 14px 12px;
  }

  .msg-body {
    max-width: 82%;
  }

  .msg-bubble {
    padding: 10px 14px;
    font-size: 0.88rem;
  }

  .chat-footer {
    padding: 12px 14px;
  }

  .input-box textarea {
    padding: 10px 14px;
    font-size: 0.88rem;
  }
}

@media (min-width: 1024px) {
  .chat-body {
    padding: 24px calc((100% - 800px) / 2 + 20px);
  }

  .chat-footer {
    padding: 16px calc((100% - 800px) / 2 + 20px);
  }
}
</style>