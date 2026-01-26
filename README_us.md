ollama -> Meta subsidiary company
Regarding the modules of ollama there are many types with support upto the 160k context . The more support it provides it requires more memmory and power.

Spring AI

What is MCP?
MCP is an open protocol that standardizes how applications provide context to LLMs. Think of MCP like a USB-C port for AI applications. Just as USB-C provides a standardized way to connect your devices to various peripherals and accessories, MCP provides a standardized way to connect AI models to different data sources and tools.


ChatModel and ChatClient

ChatModel = The engine(does the heavy lifting of actual al communication)
ChatClient = The steering wheel and dashboard(Provide an intuitive interface to control the engine)

ChatClient uses ChatModel internally but wraps it with a more convenient API. When you use ChatClient, it eventually delegates to the underlying ChatModels to make the actual AI service calls.

Example Flow:

Our Code -> ChatClient (fluent api) -> ChatModel (AI service integration) -> AI provider API


AI provider api details can be seen below:


How they work together 

Spring Boot AutoConfiguration: When you add Spring Ai dependencies, Spring Boot automatically creates ChatModel beans for configured AI providers.

ChatClient.Builder Creation: The framework provides an autoconfigured ChatClient.Builder that already is wired to the appropriate ChatModel.

Fluent API usage: You use ChatClient’s fluent methods to build prompts, which internally get converted to the format expected by the ChatModel.

Execution: ChatClient delegated to chatModel to send requests to the ai services and handle responses 

This design follows the common pattern of having a low-level chatModel / technical interface and a high-level ChatClient /user friendly interface that makes the framework more accessible while maintaining the flexibility for advanced use cases.



OpenAI
ollama/Docker hosted ollama
Other Cloud Provider
Zero setup
Setup Required
AWS/Azure/GCP
Easy to maintain
Hard to maintain by an organization
Mostly used if above mention cloud provider is already used by the organization.
Pay
Zero


Public data to be used
Private data


Famous for it’s LLM(Large language model) 






Most popular AI mode from:
・GPT-5 => openAI and Microsoft
・Claude => anthropic company
・Mistral => Microsoft and Nvidia
・Llama => meta 

The OpenAI model is baked by microsoft funding as like amazon bedrock.




Why integrate(統合) different AI Models in Spring AI
→For flexibility , performance and user experience .
→Task-based model selection: Use a powerful model for complex reasoning and light model for basic queries .
→Fallback strategy : Automatically switch to another model if one is unavailable.
→User preference-> Allow users to choose their preferred model for interaction
→Specialized model: One for code and another for the creative writing



他の大切なWindow端末コマンド：

ポート確認コマンド： Get-NetTCPConnection -LocalPort 11434 | Select-Object OwningProcess, State, LocalAddress, LocalPort | Format-Table
ollama : 
 ollama list →　モデルを一覧
ollamaリクエスト確認コマンド・操作確認： Invoke-RestMethod -Method Post -Uri "http://localhost:11434/api/chat" -Body '{"model":"gemma3:1b","messages":[{"role":"user","content":"hi"}],"stream":false}' -ContentType "application/json"
Docker：
 docker desktop enable model-runner
docker desktop enable model-runner  '--tcp=12434'









To feed the prompt we can provide the ai with the file with the extension st→String Template.
Prompt をくあうをため、
ToolResponseMessage -> Function Role
Assistance Message -> Assistant or Model Role
UserMessage -> Client/User Role
SystemMessage -> System Role
