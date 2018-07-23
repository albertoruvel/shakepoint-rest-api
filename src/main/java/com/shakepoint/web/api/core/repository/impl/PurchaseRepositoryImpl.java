package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.machine.PurchaseStatus;
import com.shakepoint.web.api.core.repository.MachineRepository;
import com.shakepoint.web.api.core.repository.PurchaseRepository;
import com.shakepoint.web.api.core.util.ShakeUtils;
import com.shakepoint.web.api.data.entity.Purchase;
import com.shakepoint.web.api.data.entity.VendingMachine;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;

@Stateless
public class PurchaseRepositoryImpl implements PurchaseRepository {

    @Inject
    private MachineRepository machineRepository;

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public PurchaseRepositoryImpl() {
    }

    @Override
    public Purchase getPurchase(String purchaseId) {
        try {
            return (Purchase) em.createQuery("SELECT p FROM Purchase p WHERE p.id = :purchaseId")
                    .setParameter("purchaseId", purchaseId).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get purchase", ex);
            return null;
        }
    }

    @Override
    public List<Purchase> getUserPurchases(String userId) {
        try {
            return em.createQuery("SELECT p FROM Purchase p WHERE p.user.id = :id")
                    .setParameter("id", userId).getResultList();
        } catch (Exception ex) {
            log.error("Could not get user purchases", ex);
            return null;
        }
    }

    @Override
    public void update(Purchase purchase) {
        em.merge(purchase);
    }

    public List<Purchase> getAvailablePurchasesForMachine(String productId, String machineId) {
        try {
            List<Purchase> list = em.createQuery("SELECT p FROM Purchase p WHERE p.machine.id = :machineId AND p.product.id = :productId AND p.status = :status")
                    .setParameter("machineId", machineId)
                    .setParameter("productId", productId)
                    .setParameter("status", PurchaseStatus.PRE_AUTH)
                    .getResultList();
            return list;
        } catch (Exception ex) {
            log.error("Could not get available purchase for machine", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public Integer getProductCountForDateRange(String id, String[] range, String machineId) {
        int counter = 0;
        Long value;
        try{
            for (String date : range){
                String format = String.format("SELECT COUNT(p.id) FROM Purchase p WHERE p.machine.id = :machineId AND p.product.id = :productId AND p.purchaseDate LIKE '%s' AND p.status <> :status", date + "%");
                value = (Long)em.createQuery(format)
                        .setParameter("machineId", machineId)
                        .setParameter("productId", id)
                        .setParameter("status", PurchaseStatus.PRE_AUTH)
                        .getSingleResult();
                counter += value;
            }
            return counter;
        }catch(Exception ex){
            log.error("Could get total products for machine and product", ex);
            return 0;
        }

    }

    @Override
    public double getTodayTotalPurchases() {
        Date date = new Date();
        String dateString = ShakeUtils.SLASHES_SIMPLE_DATE_FORMAT.format(date);
        double total = 0;
        try {
            String format = String.format("SELECT SUM(p.total) FROM Purchase p WHERE p.purchaseDate LIKE '%s' AND p.status <> :status", dateString + "%");
            BigDecimal bigDecimal = (BigDecimal) em.createQuery(format)
                    .setParameter("status", PurchaseStatus.PRE_AUTH).getSingleResult();
            if (bigDecimal == null) {
                return 0;
            }
            return bigDecimal.doubleValue();
        } catch (Exception ex) {
            log.error("Could not get today total purchases", ex);
            return 0;
        }
    }

    @Override
    public Map<String, List<Double>> getPerMachineValues(String[] range, List<VendingMachine> machines) {
        Map<String, List<Double>> map = new HashMap();
        List<Double> values;
        Double sum;
        String format;
        for (VendingMachine machine : machines) {
            values = new ArrayList();
            for (String rangeValue : range) {
                format = String.format("SELECT SUM (p.total) FROM Purchase p WHERE p.machine.id = :machineId AND p.purchaseDate LIKE '%s' AND p.status <> :status", rangeValue + "%");
                try {
                    sum = (Double) em.createQuery(format)
                            .setParameter("machineId", machine.getId())
                            .setParameter("status", PurchaseStatus.PRE_AUTH).getSingleResult();
                } catch (Exception ex) {
                    log.error("Could not get average value", ex);
                    sum = 0.0;
                }
                if (sum == null)
                    sum = 0.0;
                values.add(sum);
            }
            map.put(machine.getName(), values);
        }
        return map;
    }

    @Override
    public Map<String, List<Double>> getPerMachineProductsCountValues(String[] range, List<VendingMachine> machines) {
        Map<String, List<Double>> map = new HashMap();
        List<Double> values;
        Double avg;
        String format;
        for (VendingMachine machine : machines) {
            values = new ArrayList();
            for (String rangeValue : range) {
                format = String.format("SELECT COUNT(p.total) FROM Purchase p WHERE p.machine.id = :machineId AND p.purchaseDate LIKE '%s' AND p.status <> :status", rangeValue + "%");
                try {
                    avg = (Double) em.createQuery(format)
                            .setParameter("machineId", machine.getId())
                            .setParameter("status", PurchaseStatus.PRE_AUTH).getSingleResult();
                } catch (Exception ex) {
                    log.error("Could not get average value", ex);
                    avg = 0.0;
                }
                if (avg == null)
                    avg = 0.0;
                values.add(avg);
            }
            map.put(machine.getName(), values);
        }
        return map;
    }

    private static final String GET_TOTAL_INCOME = "select sum(p.total) from purchase p where p.purchase_date like '%s'";

    @Override
    public List<Double> getTotalIncomeValues(String[] range) {
        List<Double> values = new ArrayList();
        String format = "";
        Double total = 0.0;
        for (String s : range) {
            format = String.format(GET_TOTAL_INCOME, s + "%");
            total = (Double) em.createNativeQuery(format).getSingleResult();
            if (total == null)
                total = 0.0;
            values.add(total);
        }

        return values;
    }

    private static final String GET_TOTAL_INCOME_PER_MACHINE = "select sum(p.total) from purchase p where p.purchase_date like '%s' and p.machine_id = ?1";

    @Override
    public List<Double> getTotalIncomeValues(String[] range, String machineId) {
        List<Double> values = new ArrayList();
        String format = "";
        Double total = 0.0;
        for (String s : range) {
            format = String.format(GET_TOTAL_INCOME_PER_MACHINE, s + "%");
            total = (Double) em.createNativeQuery(format)
                    .setParameter(1, machineId)
                    .getSingleResult();
            if (total == null)
                total = 0.0;
            values.add(total);
        }

        return values;
    }

    @Override
    public List<Purchase> getAuthorizedPurchases(String userId, String machineId) {
        try {
            return em.createQuery("SELECT p FROM Purchase p WHERE p.user.id = :id AND p.machine.id = :machineId AND p.status = :status")
                    .setParameter("id", userId)
                    .setParameter("machineId", machineId)
                    .setParameter("status", PurchaseStatus.AUTHORIZED)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get active codes", ex);
            return null;
        }
    }
}
