package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.repository.PromoCodeRepository;
import com.shakepoint.web.api.data.entity.PromoCode;
import com.shakepoint.web.api.data.entity.PromoCodeRedeem;
import com.shakepoint.web.api.data.entity.PromoCodeStatus;
import com.shakepoint.web.api.data.entity.User;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
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
        return em.createQuery("SELECT p FROM Promo p WHERE p.active = :status")
                .setParameter("status", Boolean.TRUE)
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
        } catch (Exception ex) {
            logger.error("Could not persist promo code redemption", ex);
        }
    }

    @Override
    public boolean isPromoCodeAlreadyRedeemedByUser(String code, String id) {
        try {
            PromoCodeRedeem redemption = (PromoCodeRedeem) em.createQuery("SELECT r FROM PromoRedeem r WHERE r.promoCode.code = :code AND r.user.id = :userId")
                    .setParameter("code", code)
                    .setParameter("userId", id).getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Integer getPromoCodeRedemptions(String code) {
        return (Integer) em.createQuery("SELECT COUNT(r.id) FROM PromoRedeem PromoRedeem r WHERE r.code = :code")
                .setParameter("code", code).getSingleResult();
    }

    @Override
    public Integer getCountForTrainerPromoCode(String code) {
        return (Integer) em.createQuery("SELECT COUNT(pr.id) FROM PromoRedeem pr WHERE pr.promoCode.code = ?")
                .setParameter(1, code).getSingleResult();
    }

    @Override
    public List<PromoCode> getTrainerPromoCodes(String trainerId) {
        try {
            return em.createQuery("SELECT p FROM Promo p WHERE p.trainer.id = :trainerId AND p.status = :status")
                    .setParameter("status", PromoCodeStatus.ACTIVE)
                    .setParameter("trainerId", trainerId).getResultList();
        } catch (Exception ex) {
            logger.error("Could not get trainer promo codes", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public PromoCode getById(String promoCodeId) {
        try {
            return (PromoCode) em.createQuery("SELECT p FROM Promo p WHERE p.id = :id")
                    .setParameter("id", promoCodeId).getSingleResult();
        } catch(Exception ex){
            logger.error("Could not find promo code", ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void update(PromoCode promoCode) {
        try{
            em.merge(promoCode);
        }catch(Exception ex){
            logger.error("Could not merge promo code", ex);
        }
    }

}
