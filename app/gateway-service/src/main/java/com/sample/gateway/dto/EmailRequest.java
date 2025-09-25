package com.sample.gateway.dto;

public record EmailRequest(
    String to,
    String from,
    String subject,
    String body
) {}
