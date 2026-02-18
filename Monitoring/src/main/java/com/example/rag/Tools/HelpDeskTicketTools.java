package com.example.rag.Tools;

//import org.slf4j.Logger;

import com.example.rag.entity.HelpDeskTicket;
import com.example.rag.model.TicketRequest;
import com.example.rag.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class HelpDeskTicketTools {

    private final HelpDeskService helpDeskService;
    Logger logger = Logger.getLogger(HelpDeskTicketTools.class.getName());

    @Tool(name = "createTicket", description = "This is a help desk ticket tool")
    // ToolContext here will extract the context that a tool is given and not solely rely on LLM, as it is provided by the SpringAI.
    // We can retrieve this data from toolContent when tool execution is performed
    public String createTicket(TicketRequest ticketRequest, ToolContext toolContext) {
        // here in the get("username") -> the username is the Key and the toolcontext will help us extract the Value.
        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket savedTicket = helpDeskService.createTicket(ticketRequest, username);
        return "Tiket id:" + savedTicket.getId() + "Created sucssessfully for username : " + savedTicket.getUsername();

    }

    @Tool(name = "getTicketByStatus", description = "Returned status of the open ticket by name")
    public List<HelpDeskTicket> getTicketByStatus(ToolContext toolContext) {
        String username = (String) toolContext.getContext().get("username");
        return helpDeskService.getTicketByUsername(username);
    }


}
