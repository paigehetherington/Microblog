package com.theironyard;

import java.util.ArrayList;

/**
 * Created by vajrayogini on 2/22/16.
 */
public class User {
    String name;
    ArrayList <Message> messages = new ArrayList<>();


    public User(String name) {
        this.name = name;
    }

//    public User(String name, String message) {
//        this.name = name;
//        this.message = message;
//    }
}
