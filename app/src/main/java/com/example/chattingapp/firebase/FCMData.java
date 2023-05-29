package com.example.chattingapp.firebase;

public class FCMData {
    private String message;

    public FCMData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
