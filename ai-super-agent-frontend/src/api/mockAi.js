const MOCK_DELAY = 50

function mockSseStream(text, onChunk, onDone) {
  let index = 0
  const totalLength = text.length

  function sendNext() {
    if (index >= totalLength) {
      onDone()
      return
    }

    const chunkSize = Math.floor(Math.random() * 3) + 1
    const chunk = text.slice(index, index + chunkSize)
    index += chunkSize

    onChunk(chunk)

    const delay = MOCK_DELAY + Math.random() * 50
    setTimeout(sendNext, delay)
  }

  setTimeout(sendNext, 100)
}

export function fetchLoveAppChat(message, chatId, onChunk) {
  return new Promise((resolve) => {
    const response = "亲爱的，你好呀～看到你愿意和我聊聊感情话题，我真的很开心呢。不管是甜蜜的烦恼还是小小的困惑，这里都是安全的树洞。请问你现在是单身、正在恋爱中，还是已经结婚了呢？不同阶段的感情都有各自的美好和挑战，我想先了解一下你的大致情况，这样才能更好地帮助你哦～"
    mockSseStream(response, onChunk, () => {
      onChunk(null, true)
      resolve()
    })
  })
}

export function fetchManusChat(message, onChunk) {
  return new Promise((resolve) => {
    const steps = [
      "Step 1: 理解用户需求 - 正在分析你的任务...",
      "好的，我收到了你的请求。让我仔细思考一下如何最好地完成这个任务。",
      "Step 2: 规划执行方案 - 制定详细计划...",
      "根据你的需求，我计划按以下步骤执行：\n1. 先进行信息收集\n2. 分析当前情况\n3. 制定具体方案\n4. 开始执行",
      "Step 3: 开始执行 - 逐步完成任务...",
      "正在调用相关工具来帮助你完成任务第一阶段...",
      "Step 4: 验证结果 - 检查完成情况...",
      "太好了！任务的第一阶段已经完成。让我汇总一下结果：\n- 信息收集：✓ 完成\n- 初步分析：✓ 完成\n- 方案制定：✓ 完成",
      "Step 5: 收尾工作 - 最终整理...",
      "任务全部完成！有什么其他需要帮助的吗？"
    ]

    let stepIndex = 0

    function sendNextStep() {
      if (stepIndex >= steps.length) {
        onChunk(null, true)
        resolve()
        return
      }

      const step = steps[stepIndex]
      let charIndex = 0

      function sendChar() {
        if (charIndex >= step.length) {
          stepIndex++
          if (stepIndex < steps.length) {
            setTimeout(sendNextStep, 300)
          } else {
            onChunk(null, true)
            resolve()
          }
          return
        }

        const chunkSize = Math.floor(Math.random() * 4) + 1
        const chunk = step.slice(charIndex, charIndex + chunkSize)
        charIndex += chunkSize

        onChunk(chunk)

        setTimeout(sendChar, MOCK_DELAY + Math.random() * 30)
      }

      sendChar()
    }

    sendNextStep()
  })
}