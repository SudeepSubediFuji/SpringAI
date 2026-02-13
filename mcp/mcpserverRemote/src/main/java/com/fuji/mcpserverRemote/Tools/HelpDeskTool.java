package com.fuji.mcpserverRemote.Tools;
import com.fuji.mcpserverRemote.entity.HelpDeskTicket;
import com.fuji.mcpserverRemote.model.TicketRequest;
import com.fuji.mcpserverRemote.service.HelpDeskService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class HelpDeskTool {

    private final HelpDeskService helpDeskService;
    Logger logger = Logger.getLogger(HelpDeskTool.class.getName());

    @Tool(name = "createTicket", description = "This is a help desk ticket tool")
    // ToolContext here will extract the context that a tool is given and not solely rely on LLM, as it is provided by the SpringAI.
    // We can retrieve this data from toolContent when tool execution is performed
    public String createTicket(TicketRequest ticketRequest) {
        // here in the get("username") -> the username is the Key and the toolcontext will help us extract the Value.
//        String username = (String) toolContext.getContext().get("username");
        HelpDeskTicket savedTicket = helpDeskService.createTicket(ticketRequest);
        return "Tiket id:" + savedTicket.getId() + "Created sucssessfully for username : " + savedTicket.getUsername();

    }

    @Tool(name = "getTicketByStatus", description = "Returned status of the open ticket by name")
    public List<HelpDeskTicket> getTicketByStatus(@ToolParam(description = "Username to fetch the status of the tickets")String username) {
//        String username = (String) toolContext.getContext().get("username");
        logger.info("Fetching ticket from Username: "+username);
        List<HelpDeskTicket> tickets = helpDeskService.getTicketByUsername(username);
        logger.info("Tickets:  "+ tickets+"Ticket size: "+tickets.size());
        return tickets;
    }


}

