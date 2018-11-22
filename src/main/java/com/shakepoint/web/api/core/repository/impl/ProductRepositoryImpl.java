/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.repository.impl;


import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.core.repository.ProductRepository;
import com.shakepoint.web.api.data.entity.Flavor;
import com.shakepoint.web.api.data.entity.Product;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * @author Alberto Rubalcaba
 */
@Stateless
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    private final Logger log = Logger.getLogger(getClass());

    public ProductRepositoryImpl() {

    }

    @Override
    public Product getProduct(String id) {

        try {
            return (Product) em.createQuery("SELECT p FROM Product p WHERE p.id = :id")
                    .setParameter("id", id).getSingleResult();
        } catch (Exception ex) {
            log.error("Could not get product", ex);
            return null;
        }
    }

    @Override
    public List<Product> getProducts() {
        try {
            return em.createQuery("SELECT p FROM Product p").getResultList();
        } catch (Exception ex) {
            log.error("Could not get all products", ex);
            return null;
        }
    }

    @Override
    public void createProduct(Product p) {
        try {
            em.persist(p);
        } catch (Exception ex) {
            log.error("Could not create product", ex);
        }
    }

    private static final String GET_MACHINE_PRODUCTS_SIMPLE = "select p.id, p.name, p.description, p.logo_url, p.price, p.type as productType "
            + "from product p "
            + "inner join machine_product m on p.id = m.product_id "
            + "where m.machine_id = ?";

    @Override
    public List<Product> getProducts(String machineId, ProductType type) {
        try {
            return em.createNativeQuery(GET_MACHINE_PRODUCTS_SIMPLE)
                    .setParameter(1, machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get products", ex);
            return null;
        }
    }

    @Override
    @Transactional
    public void createNewFlavor(Flavor flavor) {
        try {
            em.persist(flavor);
        } catch (Exception ex) {
            log.error("Could not persist flavor entity", ex);
        }
    }

    @Override
    public List<Flavor> getFlavors() {
        return em.createQuery("SELECT f FROM Flavor f").getResultList();
    }

    @Override
    public Flavor getFlavor(String flavorId) {
        return (Flavor) em.createQuery("SELECT f FROM Flavor f WHERE f.id = :id")
                .setParameter("id", flavorId)
                .getSingleResult();
    }

    @Override
    public List<VendingMachineProductStatus> getMachineProducts(String machineId) {
        try {
            return em.createQuery("SELECT p FROM MachineProductStatus p WHERE p.machine.id = :machineId")
                    .setParameter("machineId", machineId)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Could not get machine products", ex);
            return Collections.emptyList();
        }
    }

}

