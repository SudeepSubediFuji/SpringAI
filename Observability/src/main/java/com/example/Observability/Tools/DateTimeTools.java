package com.example.Observability.Tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;

public class DateTimeTools {
    Logger logger = Logger.getLogger(DateTimeTools.class.getName());

    // This will first display japanese(lacal time) and then convert to nepali (is nepali time is requested)
//    @Tool(name = "getCurrentDateTime", description = "Current date and time provider tool")
//    String getCurrentDateTime(){
//        return LocalDateTime.now().toString();
//    }
    // This will generate the time exactly of where we ask
    @Tool(name = "getCurrentDateTime2", description = "Current date and time provider tool")
    String getCurrentDateTime2(@ToolParam(
            description = "Timezone") String timeZone) {
        logger.info("LocaleDateTime: " + LocalDateTime.now(ZoneId.of(timeZone)).toString());
        return LocalDateTime.now(ZoneId.of(timeZone)).toString();
    }
}
