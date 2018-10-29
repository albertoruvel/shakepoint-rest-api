package com.shakepoint.web.api.core.jms;

import com.shakepoint.web.api.core.service.AdminRestService;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@MessageDriven(name = "TrainersDailyPromoCodeMDB", activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "trainer_promo_promocode"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class TrainersDailyPromoMDB implements MessageListener {

    @Inject
    private AdminRestService adminRestService;

    @Override
    public void onMessage(Message message) {
        adminRestService.createTrainersDailyPromoCode();
    }
}
