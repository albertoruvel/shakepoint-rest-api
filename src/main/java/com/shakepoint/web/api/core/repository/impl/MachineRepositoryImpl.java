/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.repository.impl;

import com.shakepoint.web.api.core.repository.MachineRepository;
import com.shakepoint.web.api.core.repository.ProductRepository;
import com.shakepoint.web.api.data.entity.VendingConnection;
import com.shakepoint.web.api.data.entity.VendingMachine;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * @author Alberto Rubalcaba
 */
@Stateless
public class MachineRepositoryImpl implements MachineRepository {

    private final Logger log = Logger.getLogger(getClass());

    @Inject
    private ProductRepository productsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public MachineRepositoryImpl() {

    }

    @Override
    public List<VendingMachine> getAlertedMachines(String technicianId) {
        List<VendingMachine> page = null;
        try {
            page = entityManager.createNativeQuery("HECTO????")
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with errors", ex);
            return null;
        }
    }

    @Override
    public List<VendingMachine> getFailedMachines(String technicianId) {
        List<VendingMachine> page = null;
        try {
            page = entityManager.createNativeQuery("...............")
                    .setParameter(1, technicianId).getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get technician machines with with errors: " + ex.getMessage());
        }
        return page;
    }


    @Override
    public List<VendingMachine> getTechnicianMachines(String id) {
        try {
            return entityManager.createQuery("SELECT m FROM Machine m WHERE m.technician.id = :id")
                    .setParameter("id", id)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get technician machines" + ex);
            return null;
        }
    }

    @Override
    public boolean isMachineAlerted(String id) {
        try{
            BigInteger integer = (BigInteger)entityManager.createNativeQuery("SELECT count(*) FROM machine_product WHERE machine_id = ? AND available_percentage < 30")
                    .setParameter(1, id)
                    .getSingleResult();
            return integer.intValue() > 0;
        }catch(Exception ex){
            return false;
        }

    }

    @Override
    public List<VendingMachine> searchByName(String machineName) {
        try{
            return entityManager.createQuery("SELECT m FROM Machine m WHERE m.name LIKE :machineName")
                    .setParameter("machineName", "%" + machineName + "%")
                    .getResultList();
        }catch(Exception ex){
            return Collections.emptyList();
        }
    }

    @Override
    public VendingConnection getVendingConnection(String id) {
        return (VendingConnection)entityManager.createQuery("SELECT c FROM VendingConnection c WHERE c.machineId = :id")
                .setParameter("id", id).getSingleResult();
    }

    private static final String GET_ALERTED_MACHINES_COUNT = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30";

    @Override
    public int getAlertedMachines() {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted machines count", ex);
            return 0;
        }
    }

    private static final String GET_ACTIVE_MACHINES = "select count(*) from machine where active = 1";

    @Override
    public int getActiveMachines() {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ACTIVE_MACHINES).getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get active machines count", ex);
            return 0;
        }
    }


    private static final String DELETE_MACHINE_PRODUCT = "delete from machine_product where id = ?";

    @Override
    @Transactional
    public void deleteMachineProduct(String id) {
        //delete
        try {
            entityManager.createNativeQuery(DELETE_MACHINE_PRODUCT)
                    .setParameter(1, id).executeUpdate();
        } catch (Exception ex) {
            log.error("Could not delete machine product", ex);
        }
    }

    @Override
    public VendingMachineProductStatus getMachineProduct(String id) {
        try {
            VendingMachineProductStatus res = (VendingMachineProductStatus) entityManager.createQuery("SELECT s FROM MachineProductStatus s WHERE s.id = :id")
                    .setParameter("id", id)
                    .getSingleResult();
            return res;
        } catch (Exception ex) {
            log.error("Could not get machine product response", ex);
            return null;
        }
    }

    @Override
    public List<VendingMachineProductStatus> getMachineProducts(String machineId) {
        try{
            return entityManager.createQuery("SELECT s FROM MachineProductStatus s WHERE s.machine.id = :id")
                    .setParameter("id", machineId)
                    .getResultList();
        }catch(Exception ex){
            return null;
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addMachineProduct(VendingMachineProductStatus mp) {
        try {
            entityManager.persist(mp);
        } catch (Exception ex) {
            log.error("Could not add machine product", ex);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void addMachine(VendingMachine machine) {
        try {
            entityManager.persist(machine);
        } catch (Exception ex) {
            log.error("Could not add machine expender", ex);
        }
    }

    @Override
    public List<VendingMachine> getMachines() {
        List<VendingMachine> page = null;

        try {
            page = entityManager.createQuery("SELECT m FROM Machine m")
                    .getResultList();
            return page;
        } catch (Exception ex) {
            log.error("Could not get machines", ex);
            return page;
        }
    }

    @Override
    public VendingMachine getMachine(String machineId) {
        try {
            return (VendingMachine)entityManager.createQuery("SELECT m FROM Machine m WHERE m.id = :id")
                    .setParameter("id", machineId).getSingleResult();
        } catch (Exception ex) {
            //not found
            log.error("Error", ex);
            return null;
        }
    }


    private static final String GET_ALERTED_PRODUCTS = "select count(*) from product p inner join "
            + "machine_product mp on mp.product_id = p.id where mp.machine_id = ? and mp.available_percentage < 30";

    @Override
    public int getAlertedProducts(String machineId) {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_PRODUCTS)
                    .setParameter(1, machineId)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted products count", ex);
            return 0;
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void updateMachine(VendingMachine machine) {
        try{
            entityManager.merge(machine);
        }catch(Exception ex){
            log.error("Error trying to update machine", ex);
        }
    }

    private static final String GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN = "select count(*) from machine m "
            + "inner join machine_product mp on mp.machine_id = m.id "
            + "where mp.available_percentage < 30 and m.technician_id = ?";

    @Override
    public int getAlertedMachinesNumber(String technicianId) {

        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(GET_ALERTED_MACHINES_COUNT_BY_TECHNICIAN)
                    .setParameter(1, technicianId)
                    .getSingleResult();
            return count.intValue();
        } catch (Exception ex) {
            log.error("Could not get alerted machines by technician: " + ex.getMessage());
            return 0;
        }
    }

    private static final String CONTAINS_PRODUCT = "select count(*) from machine_product where machine_id = ? and product_id = ?";

    @Override
    public boolean containProduct(String machineId, String productId) {
        try {
            BigInteger count = (BigInteger) entityManager.createNativeQuery(CONTAINS_PRODUCT)
                    .setParameter(1, machineId)
                    .setParameter(2, productId)
                    .getSingleResult();
            return count.intValue() > 0;
        } catch (Exception ex) {
            log.error("Could not get machine_product count", ex);
            return false;
        }
    }


}
