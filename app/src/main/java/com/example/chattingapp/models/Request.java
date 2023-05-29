package com.example.chattingapp.models;

public class Request {
    private String message;

    public Request() {
        // Boş yapıcı metod
    }

    public Request(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
