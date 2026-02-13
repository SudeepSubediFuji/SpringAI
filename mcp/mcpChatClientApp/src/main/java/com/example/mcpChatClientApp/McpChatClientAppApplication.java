package com.example.mcpChatClientApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
//        (exclude = {
//        org.springframework.ai.autoconfigure.mcp.client.SseHttpClientTransportAutoConfiguration.class
//})
public class McpChatClientAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpChatClientAppApplication.class, args);
	}

}
