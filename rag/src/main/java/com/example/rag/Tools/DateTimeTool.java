package com.example.rag.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeTool {
    @Tool(name="getCurrenLocaletDateTime" ,description = "Current date and time provider tool")
    String getCurrenLocaletDateTime(){
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }
}
