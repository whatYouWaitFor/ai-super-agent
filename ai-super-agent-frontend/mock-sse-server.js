const http = require('http')

const MOCK_TEXT = '亲爱的，你好呀～看到你愿意和我聊聊感情话题，我真的很开心呢。不管是甜蜜的烦恼还是小小的困惑，这里都是安全的树洞。请问你现在是单身、正在恋爱中，还是已经结婚了呢？不同阶段的感情都有各自的美好和挑战，我想先了解一下你的大致情况，这样才能更好地帮助你哦～'

function sendSseEmitter(res) {
  res.writeHead(200, {
    'Content-Type': 'text/event-stream',
    'Cache-Control': 'no-cache',
    'Connection': 'keep-alive',
    'Access-Control-Allow-Origin': '*'
  })

  let index = 0
  const interval = setInterval(() => {
    if (index >= MOCK_TEXT.length) {
      res.write('data: [DONE]\n\n')
      clearInterval(interval)
      res.end()
      return
    }

    const chunkSize = Math.floor(Math.random() * 3) + 1
    const chunk = MOCK_TEXT.slice(index, index + chunkSize)
    index += chunkSize

    res.write(`data: ${chunk}\n\n`)
  }, 50)
}

const server = http.createServer((req, res) => {
  if (req.url.startsWith('/api/ai/loveApp/chat')) {
    sendSseEmitter(res)
  } else if (req.url.startsWith('/api/ai/myManus/chat')) {
    const manusText = '你好！我是 AI 超级智能体。正在分析你的任务...第一步完成，开始执行...任务完成！'
    let index = 0
    const interval = setInterval(() => {
      if (index >= manusText.length) {
        res.write('data: [DONE]\n\n')
        clearInterval(interval)
        res.end()
        return
      }
      const chunkSize = Math.floor(Math.random() * 4) + 1
      const chunk = manusText.slice(index, index + chunkSize)
      index += chunkSize
      res.write(`data: ${chunk}\n\n`)
    }, 80)
  } else {
    res.writeHead(404)
    res.end()
  }
})

server.listen(8080, () => {
  console.log('Mock SSE server running on http://localhost:8080')
  console.log('Test URLs:')
  console.log('  http://localhost:8080/api/ai/loveApp/chat?message=hi&chatId=test')
  console.log('  http://localhost:8080/api/ai/myManus/chat?message=hi')
})