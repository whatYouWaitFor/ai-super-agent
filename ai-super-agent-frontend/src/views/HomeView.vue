<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const apps = [
  {
    id: 'love',
    name: 'AI 恋爱大师',
    description: '专业的恋爱咨询助手，帮你解决情感困惑',
    icon: '💕',
    color: '#ff6b9d',
    route: '/love-app',
  },
  {
    id: 'manus',
    name: 'AI 超级智能体',
    description: '强大的智能代理，帮你完成各种复杂任务',
    icon: '🤖',
    color: '#6c5ce7',
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
      <h1>AI 超级智能体平台</h1>
      <p>选择一个应用开始体验</p>
    </div>
    <div class="app-grid">
      <div
        v-for="app in apps"
        :key="app.id"
        class="app-card"
        :style="{ '--card-color': app.color }"
        @click="navigateTo(app.route)"
      >
        <div class="app-icon">{{ app.icon }}</div>
        <div class="app-info">
          <h2>{{ app.name }}</h2>
          <p>{{ app.description }}</p>
        </div>
        <div class="app-arrow">→</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.home-header {
  text-align: center;
  margin-bottom: 60px;
}

.home-header h1 {
  font-size: 2.4rem;
  color: #1a1a2e;
  margin-bottom: 12px;
  font-weight: 700;
}

.home-header p {
  font-size: 1.1rem;
  color: #666;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  gap: 24px;
  max-width: 800px;
  width: 100%;
}

.app-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px 28px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
}

.app-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  border-color: var(--card-color);
}

.app-icon {
  font-size: 3rem;
  width: 72px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 16px;
  background: linear-gradient(135deg, var(--card-color)15, var(--card-color)08);
  flex-shrink: 0;
}

.app-info {
  flex: 1;
}

.app-info h2 {
  font-size: 1.3rem;
  color: #1a1a2e;
  margin-bottom: 6px;
  font-weight: 600;
}

.app-info p {
  font-size: 0.9rem;
  color: #888;
  line-height: 1.4;
}

.app-arrow {
  font-size: 1.4rem;
  color: #ccc;
  transition: all 0.3s ease;
}

.app-card:hover .app-arrow {
  color: var(--card-color);
  transform: translateX(4px);
}
</style>
