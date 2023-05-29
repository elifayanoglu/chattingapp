package com.example.chattingapp.firebase;

public class FCMMessage {
    private String to;
    private FCMNotification notification;
    private FCMData data;

    public FCMMessage(String to, FCMNotification notification, FCMData data) {
        this.to = to;
        this.notification = notification;
      //  this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public FCMNotification getNotification() {
        return notification;
    }

    public void setNotification(FCMNotification notification) {
        this.notification = notification;
    }

  /*  public FCMData getData() {
        return data;
    }

    public void setData(FCMData data) {
        this.data = data;
    }*/
}

