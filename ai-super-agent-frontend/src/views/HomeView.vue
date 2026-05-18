<script setup>
import { useRouter } from 'vue-router'
import { useSeo } from '../composables/useSeo.js'

useSeo({
  title: '首页',
  description: 'AI 超级智能体平台首页，提供 AI 恋爱大师和 AI 超级智能体两大智能服务，选择您需要的 AI 助手开始体验。',
})

const router = useRouter()

const apps = [
  {
    id: 'love',
    name: 'AI 恋爱大师',
    description: '专业的恋爱咨询助手，帮你解决情感困惑',
    icon: '💕',
    color: '#ff6b9d',
    gradient: 'linear-gradient(135deg, #ff6b9d, #ff8a80)',
    route: '/love-app',
  },
  {
    id: 'manus',
    name: 'AI 超级智能体',
    description: '强大的智能代理，帮你完成各种复杂任务',
    icon: '🤖',
    color: '#6c5ce7',
    gradient: 'linear-gradient(135deg, #6c5ce7, #a29bfe)',
    route: '/manus-app',
  },
]

function navigateTo(route) {
  router.push(route)
}
</script>

<template>
  <div class="home">
    <div class="home-header">
      <div class="logo">✨</div>
      <h1>AI 超级智能体平台</h1>
      <p>选择一个应用，开启智能体验</p>
    </div>
    <div class="app-grid">
      <div
        v-for="app in apps"
        :key="app.id"
        class="app-card"
        @click="navigateTo(app.route)"
      >
        <div class="card-icon" :style="{ background: app.gradient }">
          {{ app.icon }}
        </div>
        <div class="card-body">
          <h2>{{ app.name }}</h2>
          <p>{{ app.description }}</p>
        </div>
        <div class="card-arrow">→</div>
      </div>
    </div>
    <footer class="home-footer">
      <p>Powered by Spring AI & Vue 3</p>
    </footer>
  </div>
</template>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
  background: linear-gradient(180deg, #f8f9fc 0%, #eef0f8 100%);
}

.home-header {
  text-align: center;
  margin-bottom: 56px;
}

.logo {
  font-size: 3rem;
  margin-bottom: 16px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

.home-header h1 {
  font-size: 2.2rem;
  color: var(--color-text);
  margin-bottom: 8px;
  font-weight: 800;
  letter-spacing: -0.5px;
}

.home-header p {
  font-size: 1.05rem;
  color: var(--color-text-secondary);
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
  max-width: 760px;
  width: 100%;
}

.app-card {
  background: var(--color-surface);
  border-radius: var(--radius-lg);
  padding: 28px 24px;
  display: flex;
  align-items: center;
  gap: 18px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1.5px solid transparent;
  box-shadow: var(--shadow-sm);
  position: relative;
  overflow: hidden;
}

.app-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: var(--card-color, #6c5ce7);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--card-color, #6c5ce7);
}

.app-card:hover::before {
  transform: scaleX(1);
}

.card-icon {
  font-size: 2.2rem;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-md);
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-body {
  flex: 1;
  min-width: 0;
}

.card-body h2 {
  font-size: 1.15rem;
  color: var(--color-text);
  margin-bottom: 4px;
  font-weight: 700;
}

.card-body p {
  font-size: 0.85rem;
  color: var(--color-text-muted);
  line-height: 1.4;
}

.card-arrow {
  font-size: 1.3rem;
  color: #ccc;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.app-card:hover .card-arrow {
  color: var(--card-color, #6c5ce7);
  transform: translateX(4px);
}

.home-footer {
  margin-top: 56px;
  text-align: center;
}

.home-footer p {
  font-size: 0.8rem;
  color: var(--color-text-muted);
}

@media (max-width: 640px) {
  .home {
    padding: 32px 16px;
    justify-content: flex-start;
    padding-top: 80px;
  }

  .home-header {
    margin-bottom: 40px;
  }

  .home-header h1 {
    font-size: 1.6rem;
  }

  .app-grid {
    grid-template-columns: 1fr;
    gap: 14px;
  }

  .app-card {
    padding: 22px 18px;
    gap: 14px;
  }

  .card-icon {
    width: 50px;
    height: 50px;
    font-size: 1.8rem;
  }

  .card-body h2 {
    font-size: 1.05rem;
  }
}

@media (min-width: 641px) and (max-width: 1024px) {
  .app-grid {
    max-width: 600px;
  }
}
</style>