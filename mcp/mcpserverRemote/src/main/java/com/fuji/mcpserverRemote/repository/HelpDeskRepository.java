package com.fuji.mcpserverRemote.repository;

import com.fuji.mcpserverRemote.entity.HelpDeskTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface HelpDeskRepository extends JpaRepository<HelpDeskTicket, Long> {
    List<HelpDeskTicket> findByUsername(String username);
}
