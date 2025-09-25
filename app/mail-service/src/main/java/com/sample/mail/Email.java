package com.sample.mail;

public record Email(
    String to,
    String from,
    String subject,
    String body
) {}
