package com.example.Observability.Tools;

import com.example.Observability.entity.HelpDeskTicket;
import com.example.Observability.model.TicketRequest;
import com.example.Observability.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class TicketCreate {
    private final HelpDeskService helpDeskService;
    Logger logger = Logger.getLogger(HelpDeskTicketTools.class.getName());
    @Tool(name = "createTicket2", description = "This is a help desk ticket tool" ,returnDirect = true)
    // ToolContext here will extract the context that a tool is given and not solely rely on LLM, as it is provided by the SpringAI.
    // We can retrieve this data from toolContent when tool execution is performed
    public String createTicket2(TicketRequest ticketRequest, ToolContext toolContext) {
        // here in the get("username") -> the username is the Key and the toolcontext will help us extract the Value.
        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket savedTicket = helpDeskService.createTicket(ticketRequest, username);
        return "Tiket id:" + savedTicket.getId() + "Created sucssessfully for username : " + savedTicket.getUsername();
//        throw new RuntimeException("issue");
    }
}
