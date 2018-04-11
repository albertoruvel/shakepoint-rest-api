package com.shakepoint.web.api.core.factory;

import com.shakepoint.integration.jms.client.handler.JmsHandler;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;

public class JmsFactory {

    @Resource(lookup = "java:jboss/exported/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    private Logger log = Logger.getLogger(getClass());

    @Produces
    @Named(value = "jmsHandler")
    @ApplicationScoped
    public JmsHandler jmsHandler(){
        return new JmsHandler(connectionFactory, log);
    }

}
