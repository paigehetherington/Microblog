package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static User user;

    public static void main(String[] args) {
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
                    user = new User(name);
                    response.redirect("/");
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
