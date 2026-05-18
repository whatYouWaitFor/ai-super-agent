<script setup>
import { ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { fetchManusChat } from '../api/ai.js'
import { useSeo } from '../composables/useSeo.js'

useSeo({
  title: 'AI 超级智能体',
  description: 'AI 超级智能体，强大的 AI 任务助手，可以帮你完成各种复杂任务，包括信息检索、数据分析、内容创作等，是你的全能 AI 代理。',
})

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatContainer = ref(null)

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
    await fetchManusChat(message, (chunk, done) => {
      if (done) {
        isLoading.value = false
        return
      }
      if (chunk) {
        const cleaned = chunk.replace(/^Step \d+: /, '')
        if (messages.value[aiIndex].content) {
          messages.value[aiIndex].content += '\n\n'
        }
        messages.value[aiIndex].content += cleaned
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
        <span class="header-icon">🤖</span>
        <h1>AI 超级智能体</h1>
      </div>
      <span class="chat-badge">Manus</span>
    </header>

    <main class="chat-body" ref="chatContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">🤖</div>
        <h2>你好，我是 AI 超级智能体</h2>
        <p>我可以帮你完成各种复杂任务，尽管吩咐～</p>
      </div>

      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-row', msg.role === 'user' ? 'row-user' : 'row-ai']"
      >
        <div class="msg-avatar">{{ msg.role === 'user' ? '🧑' : '🤖' }}</div>
        <div class="msg-body">
          <div class="msg-bubble">{{ msg.content }}</div>
        </div>
      </div>

      <div v-if="isLoading && messages[messages.length - 1]?.content === ''" class="message-row row-ai">
        <div class="msg-avatar">🤖</div>
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
          placeholder="输入你的任务..."
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
  background: #f0f4ff;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 14px 20px;
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  color: #fff;
  gap: 14px;
  flex-shrink: 0;
  box-shadow: 0 2px 12px rgba(108, 92, 231, 0.25);
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
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
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
  border-top: 1px solid #e8e0f0;
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
  border: 2px solid #e8e0f0;
  border-radius: var(--radius-xl);
  font-size: 0.92rem;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
  max-height: 120px;
  transition: border-color 0.2s;
  background: #f8f7ff;
}

.input-box textarea:focus {
  border-color: #6c5ce7;
  background: #fff;
}

.send-btn {
  width: 42px;
  height: 42px;
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
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
  box-shadow: 0 4px 14px rgba(108, 92, 231, 0.4);
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