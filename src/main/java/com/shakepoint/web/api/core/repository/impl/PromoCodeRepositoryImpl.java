package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.repository.PromoCodeRepository;
import com.shakepoint.web.api.data.entity.PromoCode;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PromoCodeRepositoryImpl implements PromoCodeRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void createPromoCode(PromoCode promoCode) {
        try{
            em.persist(promoCode);
        }catch(Exception ex){
            logger.error("Could not persist promo code");
        }
    }
}
