package com.fuji.mcpserverstdio.model;

// the Model (often called an Entity) is a simple Java class (POJO) that represents a table in your database.
public record TicketRequest(String issue, String username) {
}
