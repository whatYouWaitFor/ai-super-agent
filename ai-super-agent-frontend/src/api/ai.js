const API_BASE = '/api'

export function fetchLoveAppChat(message, chatId, onChunk) {
  const url = `${API_BASE}/ai/loveApp/chat?message=${encodeURIComponent(message)}&chatId=${encodeURIComponent(chatId)}`

  return fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const reader = response.body.getReader()
      const decoder = new TextDecoder()
      let buffer = ''
      let lastContent = ''

      function read() {
        return reader.read().then(({ done, value }) => {
          if (done) {
            onChunk(null, true)
            return
          }
          buffer += decoder.decode(value, { stream: true })
          const lines = buffer.split('\n')
          buffer = lines.pop() || ''

          for (const line of lines) {
            if (line.startsWith('data:')) {
              let data = line.slice(5).trim()
              if (data && data !== '[DONE]') {
                if (data.startsWith(lastContent)) {
                  data = data.slice(lastContent.length)
                }
                lastContent += data
                if (data) {
                  onChunk(data, false)
                }
              }
            }
          }
          return read()
        })
      }

      return read()
    })
}

export function fetchManusChat(message, onChunk) {
  const url = `${API_BASE}/ai/myManus/chat?message=${encodeURIComponent(message)}`

  return fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      const reader = response.body.getReader()
      const decoder = new TextDecoder()
      let buffer = ''
      let lastContent = ''

      function read() {
        return reader.read().then(({ done, value }) => {
          if (done) {
            onChunk(null, true)
            return
          }
          buffer += decoder.decode(value, { stream: true })
          const lines = buffer.split('\n')
          buffer = lines.pop() || ''

          for (const line of lines) {
            if (line.startsWith('data:')) {
              let data = line.slice(5).trim()
              if (data && data !== '[DONE]') {
                if (data.startsWith(lastContent)) {
                  data = data.slice(lastContent.length)
                }
                lastContent += data
                if (data) {
                  onChunk(data, false)
                }
              }
            }
          }
          return read()
        })
      }

      return read()
    })
}
