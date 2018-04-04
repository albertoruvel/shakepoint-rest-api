package com.shakepoint.web.api.core.service.email;


import java.util.Map;

public interface EmailSender {
    void sendEmail(final String emailAsJson);
    void sendEmail(String to, Template emailTemplate, Map<String, Object> params);
}
