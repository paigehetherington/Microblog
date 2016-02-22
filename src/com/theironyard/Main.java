package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user;

    public static void main(String[] args) {
        HashMap<String, User> allUsers = new HashMap<>();
        User doug = new User("Doug", "1234");
        allUsers.put(doug.name, doug);
        Spark.init();

        Spark.get( //takes 3 arguments
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    if (user == null) {
                        return new ModelAndView(m, "index.html");
                        //injecting values into template
                    } else {
                        m.put("name", user.name); //adds name and messages to HM
                        m.put("messages", user.messages);
                        return new ModelAndView(m, "messages.html");
                    }


                }),
                new MustacheTemplateEngine()
        );

        Spark.post( //redirects to get route
                "/create-user",
                ((request, response) -> { //anon fxn
                    String name = request.queryParams("indexName");
                    String password = request.queryParams("password");
                    if (!allUsers.containsKey(name)) {
                        user = new User(name, password);
                        allUsers.put(name, user);
                        response.redirect("/");
                    } else {
                        if (password.equalsIgnoreCase(allUsers.get(name).password)){
                            user = new User (name, password);
                            response.redirect("/");
                        } else{
                            user = null;
                            response.redirect("/");
                        }
                    }
                    //    user = new User(name);
                    //} else {
                     //   if (
                    //}
                    //check to see if allUsers has a key of indexName,
                    //if they don't create user object with indexName and password
                    //if they do exist, you compare user.password to password

                    //user = new User(name);

                    return "";
                })

        );
        Spark.post(
            "/create-message",
                (((request, response) ->  {
                    String text = request.queryParams("newMessage");
                    Message message = new Message(text);
                    user.messages.add(message);
                    response.redirect("/");
                    return "";
                }))
        );
    }

}
