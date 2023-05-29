package com.example.chattingapp.models;

import java.io.Serializable;

public class User implements Serializable {
    public String  id, name, image, email, token;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
