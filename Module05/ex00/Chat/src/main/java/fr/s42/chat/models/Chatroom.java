package fr.s42.chat.models;

import java.util.Objects;
import java.util.List;

public class Chatroom {
    private Long id;
    private String name;
    private User onwer;
    private List<Message> messages;
    //getters	
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public User getOnwer() {
        return onwer;
    }
    public List<Message> getMessages() {
        return messages;
    }
    // setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOnwer(User onwer) {
        this.onwer = onwer;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Chatroom chatroom = (Chatroom)obj;
        return Objects.equals(id, chatroom.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    @Override
    public String toString() {
        return "id = "+id+", name = "+name+", owner = "+onwer;
    }
}

