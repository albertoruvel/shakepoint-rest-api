package com.shakepoint.web.api.core.service.email.impl;

import com.shakepoint.email.model.Email;
import com.shakepoint.web.api.core.service.email.EmailAsyncSender;
import com.shakepoint.web.api.core.service.email.EmailSender;
import com.shakepoint.web.api.core.service.email.EmailSenderThread;
import com.shakepoint.web.api.core.service.email.Template;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

public class EmailAsyncSenderImpl implements EmailAsyncSender {

    private final Logger log = Logger.getLogger(getClass());


    @Inject
    private EmailSender emailSender;

    protected void sendEmail(String to, String subject, String templateName, Map<String, Object> params) {
        if(params == null)
            params = Collections.EMPTY_MAP;

        final String emailAsJson = new Email(to, templateName, subject, params).toJson();

        log.info("Sending email to "+to);

        new EmailSenderThread(emailAsJson, emailSender).run();
    }

    public void sendEmail(String to, Template emailTemplate, Map<String, Object> params) {
        sendEmail(to, emailTemplate.getSubject(), emailTemplate.getTemplateName(), params);
    }
}
