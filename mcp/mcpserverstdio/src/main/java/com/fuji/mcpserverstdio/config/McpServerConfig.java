package com.fuji.mcpserverRemote.config;
import com.fuji.mcpserverRemote.Tools.HelpDeskTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpServerConfig {
    @Bean
    public List<ToolCallback> toolCallbacks(HelpDeskTool helpDeskTool){
        return List.of(ToolCallbacks.from(helpDeskTool));
    }

}

//   C:\\Users\\SudeepFuji\\.m2\\repository\\com\\fuji\\mcpserverstdio\\0.0.1-SNAPSHOT\\mcpserverstdio-0.0.1-SNAPSHOT.jar
// -jar C:\\Users\\SudeepFuji\\Study\\Java\\SpringAI\\mcpserverstdio\\target\\mcpserverstdio-0.0.1-SNAPSHOT.jar

//C:\\Users\\SudeepFuji\\Study\\Java\\SpringAI\\rag\\target\\rag-0.0.1-SNAPSHOT.jar
