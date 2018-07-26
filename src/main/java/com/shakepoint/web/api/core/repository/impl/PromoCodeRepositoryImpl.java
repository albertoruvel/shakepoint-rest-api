package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.repository.PromoCodeRepository;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoCodeRedeem;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PromoCodeRepositoryImpl implements PromoCodeRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void createPromoCode(PromoCode promoCode) {
        try {
            em.persist(promoCode);
        } catch (Exception ex) {
            logger.error("Could not persist promo code");
        }
    }

    @Override
    public List<PromoCode> getAllPromoCodes() {
        return em.createQuery("SELECT p FROM Promo p")
                .getResultList();
    }

    @Override
    public PromoCode findPromoCodeByCode(String code) {
        try {
            return (PromoCode) em.createQuery("SELECT p FROM Promo p WHERE p.code = :code")
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void redeemPromoCode(PromoCodeRedeem redemption) {
        try {
            em.persist(redemption);
        }catch(Exception ex){
            logger.error("Could not persist promo code redemption", ex);
        }
    }

    @Override
    public boolean isPromoCodeAlreadyRedeemedByUser(String code, String id) {
        try{
            PromoCodeRedeem redemption = (PromoCodeRedeem) em.createQuery("SELECT r FROM PromoRedeem r WHERE r.promoCode.code = :code AND r.user.id = :userId")
                    .setParameter("code", code)
                    .setParameter("userId", id).getSingleResult();
            return true;
        }catch(Exception ex){
            return false;
        }
    }
}
