package org.server.coursework;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageBuilder {
    private static int idCounter;

    @JsonProperty("id")
    private int id;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("message")
    private String message;


    public MessageBuilder(String sender, String message) {
        this.id = idCounter++;
        this.sender = sender;
        this.message = message.trim();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String toJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
