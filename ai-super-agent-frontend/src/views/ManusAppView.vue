<script setup>
import { ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { fetchManusChat } from '../api/ai.js'

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

  const aiMessage = { role: 'ai', content: '' }
  messages.value.push(aiMessage)
  scrollToBottom()

  try {
    await fetchManusChat(message, (chunk, done) => {
      if (done) {
        isLoading.value = false
        return
      }
      if (chunk) {
        aiMessage.content += chunk
        scrollToBottom()
      }
    })
  } catch (error) {
    aiMessage.content += '\n\n❌ 请求失败，请稍后重试'
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
  <div class="chat-page manus-app">
    <div class="chat-header">
      <button class="back-btn" @click="goHome">← 返回</button>
      <h1>🤖 AI 超级智能体</h1>
      <span class="header-badge">Manus</span>
    </div>
    <div class="chat-container" ref="chatContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">🤖</div>
        <p>你好！我是 AI 超级智能体，可以帮你完成各种复杂任务～</p>
      </div>
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.role === 'user' ? 'message-user' : 'message-ai']"
      >
        <div class="message-avatar">
          {{ msg.role === 'user' ? '🧑' : '🤖' }}
        </div>
        <div class="message-content">
          <div class="message-bubble">{{ msg.content }}</div>
        </div>
      </div>
      <div v-if="isLoading && messages[messages.length - 1]?.content === ''" class="message message-ai">
        <div class="message-avatar">🤖</div>
        <div class="message-content">
          <div class="message-bubble typing">思考中<span class="dot">.</span><span class="dot">.</span><span class="dot">.</span></div>
        </div>
      </div>
    </div>
    <div class="chat-input-area">
      <div class="input-wrapper">
        <textarea
          v-model="inputMessage"
          @keydown="handleKeydown"
          placeholder="输入你的任务..."
          rows="1"
          :disabled="isLoading"
        ></textarea>
        <button class="send-btn" @click="sendMessage" :disabled="isLoading || !inputMessage.trim()">
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f4ff;
}

.chat-header {
  display: flex;
  align-items: center;
  padding: 16px 24px;
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  color: #fff;
  gap: 16px;
  flex-shrink: 0;
}

.chat-header h1 {
  font-size: 1.2rem;
  font-weight: 600;
  flex: 1;
}

.header-badge {
  font-size: 0.75rem;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 10px;
  border-radius: 12px;
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: #fff;
  padding: 6px 14px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background 0.2s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.35);
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px 24px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 16px;
}

.empty-state p {
  font-size: 1rem;
}

.message {
  display: flex;
  margin-bottom: 20px;
  gap: 12px;
}

.message-user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  flex-shrink: 0;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.message-content {
  max-width: 70%;
}

.message-bubble {
  padding: 12px 18px;
  border-radius: 18px;
  line-height: 1.6;
  font-size: 0.95rem;
  white-space: pre-wrap;
  word-break: break-word;
}

.message-user .message-bubble {
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  color: #fff;
  border-bottom-right-radius: 4px;
}

.message-ai .message-bubble {
  background: #fff;
  color: #333;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.typing .dot {
  animation: blink 1.4s infinite both;
}

.typing .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0%, 80%, 100% {
    opacity: 0;
  }
  40% {
    opacity: 1;
  }
}

.chat-input-area {
  padding: 16px 24px;
  background: #fff;
  border-top: 1px solid #e8e0f0;
  flex-shrink: 0;
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-wrapper textarea {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e8e0f0;
  border-radius: 12px;
  font-size: 0.95rem;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
  max-height: 120px;
  transition: border-color 0.2s;
}

.input-wrapper textarea:focus {
  border-color: #6c5ce7;
}

.send-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 600;
  white-space: nowrap;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(108, 92, 231, 0.4);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
