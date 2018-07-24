package com.shakepoint.web.api.core.jms;

import com.shakepoint.web.api.core.service.AdminRestService;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@MessageDriven(name = "BirthDatePromoCodeMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "birthdate_promocode"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class BirthDatePromoCodeMDB implements MessageListener {

    private final Logger logger = Logger.getLogger(getClass());

    @Inject
    private AdminRestService adminRestService;

    @Override
    public void onMessage(Message message)  {
        try{
            TextMessage textMessage = (TextMessage)message;
            //get user id
            String userId = textMessage.getText();
            adminRestService.createBirthDatePromoCode(userId);
        }catch(JMSException ex){
            logger.error("Could not read JMS message", ex);
        }
    }
}
