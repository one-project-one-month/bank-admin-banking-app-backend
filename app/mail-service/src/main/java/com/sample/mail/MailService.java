package com.sample.mail;

import io.javalin.Javalin;

public class MailService {

    public static void main(String[] args) {
        EmailConsumer emailConsumer = new EmailConsumer();
        Thread consumerThread = new Thread(emailConsumer);
        consumerThread.start();

        Javalin app = Javalin.create().start(7000);

        app.get("/", ctx -> ctx.result("Mail Service is running."));
    }
}
