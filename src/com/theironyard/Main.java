package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Main {

    //static User user;

    public static void main(String[] args) {
        HashMap<String, User> allUsers = new HashMap<>();
        User doug = new User("Doug", "1234");
        allUsers.put(doug.name, doug);

        Spark.staticFileLocation("public"); //tells server where files are for CSS

        Spark.init();

        Spark.get( //method call takes 3 arguments, split on 3 lines
                "/",
                ((request, response) -> { //anonymous fxn
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = allUsers.get(name);

                    HashMap m = new HashMap();

                    if (user == null) {
                        return new ModelAndView(m, "index.html"); // matches values in java (HM) with html, server side html
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
                        User user = new User(name, password);
                        allUsers.put(name, user);
                        response.redirect("/");
                    } else {
                        if (password.equals(allUsers.get(name).password)){
                            //user = new User (name, password);
                            response.redirect("/");
                        } else {
                            Spark.halt(403);
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

                    Session session = request.session();
                    session.attribute("userName", name);

                    return "";
                })

        );

        Spark.post(
            "/create-message",
                ((request, response) ->  {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = allUsers.get(name);
                    String text = request.queryParams("newMessage");
                    Message message = new Message(text);
                    user.messages.add(message); //add to AL of messages
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/delete-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = allUsers.get(name);
                    String deleteNum = request.queryParams("deleteNum");
                    int idNum = Integer.valueOf(deleteNum);
                    user.messages.remove(idNum -1);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName");
                    User user = allUsers.get(name);
                    String editNum = request.queryParams("editNum");
                    int idNum = Integer.valueOf(editNum);
                    Message edit = user.messages.get(idNum - 1);
                    String editMessage = request.queryParams("editMessage");
                    edit.message = editMessage;
                    response.redirect("/");
                    return "";


                })
        );

        Spark.post(
                "/logout",
                ((request, response) ->  {
                    Session session = request.session(); //session keeps track of anything server side. attribute like get/put
                    session.invalidate();
                    response.redirect("/");
                    return "";
                })
        );
    }

}
