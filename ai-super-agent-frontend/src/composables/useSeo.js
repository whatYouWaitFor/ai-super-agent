import { onMounted, onUnmounted } from 'vue'

const DEFAULT_TITLE = 'AI 超级智能体平台'
const DEFAULT_DESCRIPTION = 'AI 超级智能体平台，提供 AI 恋爱大师情感咨询和 AI 超级智能体任务助手两大服务，基于大语言模型，为您提供智能、贴心的 AI 对话体验。'

export function useSeo({ title, description } = {}) {
  const pageTitle = title ? `${title} - ${DEFAULT_TITLE}` : DEFAULT_TITLE
  const pageDescription = description || DEFAULT_DESCRIPTION

  let originalTitle = document.title
  let originalDescription = ''

  onMounted(() => {
    originalTitle = document.title
    document.title = pageTitle

    const metaDesc = document.querySelector('meta[name="description"]')
    if (metaDesc) {
      originalDescription = metaDesc.getAttribute('content') || ''
      metaDesc.setAttribute('content', pageDescription)
    }

    const ogTitle = document.querySelector('meta[property="og:title"]')
    if (ogTitle) ogTitle.setAttribute('content', pageTitle)

    const ogDesc = document.querySelector('meta[property="og:description"]')
    if (ogDesc) ogDesc.setAttribute('content', pageDescription)

    const twitterTitle = document.querySelector('meta[name="twitter:title"]')
    if (twitterTitle) twitterTitle.setAttribute('content', pageTitle)

    const twitterDesc = document.querySelector('meta[name="twitter:description"]')
    if (twitterDesc) twitterDesc.setAttribute('content', pageDescription)
  })

  onUnmounted(() => {
    document.title = originalTitle

    const metaDesc = document.querySelector('meta[name="description"]')
    if (metaDesc) metaDesc.setAttribute('content', originalDescription)
  })
}