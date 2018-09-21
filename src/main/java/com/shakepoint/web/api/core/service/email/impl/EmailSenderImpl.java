package com.shakepoint.web.api.core.service.email.impl;

import com.github.roar109.syring.annotation.ApplicationProperty;
import com.shakepoint.email.EmailQueue;
import com.shakepoint.email.model.Email;
import com.shakepoint.integration.jms.client.handler.JmsHandler;
import com.shakepoint.web.api.core.service.email.EmailSender;
import com.shakepoint.web.api.core.service.email.EmailSenderThread;
import com.shakepoint.web.api.core.service.email.Template;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.jms.ConnectionFactory;
import java.util.Collections;
import java.util.Map;


@Startup
public class EmailSenderImpl implements EmailSender {

    private final Logger log = Logger.getLogger(getClass());

    @Inject
    @Named(value = "jmsHandler")
    private JmsHandler jmsHandler;

    @Inject
    @ApplicationProperty(name = "com.shakepoint.web.email.enabled", type = ApplicationProperty.Types.SYSTEM)
    private String emailEnabled;

    public void sendEmail(final String emailAsJson) {
        if (Boolean.parseBoolean(emailEnabled)) {
            jmsHandler.send(EmailQueue.NAME, emailAsJson);
        } else {
            log.info("Sending emails is disabled in standalone");
        }
    }

    protected void sendEmail(String to, String subject, String templateName, Map<String, Object> params) {
        if (params == null)
            params = Collections.EMPTY_MAP;
        final String emailAsJson = new Email(to, templateName, subject, params).toJson();
        new EmailSenderThread(emailAsJson, this);
    }

    @Override
    public void sendEmail(String to, Template emailTemplate, Map<String, Object> params) {
        sendEmail(to, emailTemplate.getSubject(), emailTemplate.getTemplateName(), params);
    }
}
