package com.example.android.chatwithbot;

/**
 * Created by safwanx on 11/22/17.
 */

public class ChatMessage {
    public int type;
    public String message;
    public long id;
    public long timeStamp;

    public long getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChatMessage(int type, String message, long id, long timeStamp) {
        super();
        this.type = type;
        this.message = message;
        this.id = id;
        this.timeStamp = timeStamp;
    }

    public ChatMessage(int type, String message, long timeStamp) {
        super();
        this.type = type;
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
