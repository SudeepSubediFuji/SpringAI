package com.example.rag.service;

import com.example.rag.entity.HelpDeskTicket;
import com.example.rag.model.TicketRequest;
import com.example.rag.repository.HelpDeskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskService {
    // Above RequiredArgsConstructor will create a constructor as looking at the dependency helpDeskRepository.
    private final HelpDeskRepository helpDeskRepository;
//    public List<HelpDeskTicket> findByuser(String username){
//        return helpDeskRepository.findBy(username);
//    }


    // HelpDeskTicket entity
    // ticketRequest is a model which will record the issues.
    public HelpDeskTicket createTicket(TicketRequest ticketRequest, String username){
        HelpDeskTicket helpDeskTicket = HelpDeskTicket.builder()
                .issue(ticketRequest.issue())
                .username(username)
                .status("Open")
                .created_at(LocalDateTime.now())
                .eta(LocalDateTime.now().plusDays(7))
                .build();

        // Here above we are building a new record by adding required data(table-name:,username,status,created_at)--> Just populating data
        // here in helpDeskRepository table we are going to save in DB
        return helpDeskRepository.save(helpDeskTicket);
    }

    // Service created here will be used by repository
    public List<HelpDeskTicket> getTicketByUsername(String username){
        // Here we will invoke the findByUser method of helpDeskRepository and output the list of HelpDeskTicket entity data
        return helpDeskRepository.findByUsername(username);
    }


}
