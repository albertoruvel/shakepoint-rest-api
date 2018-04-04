/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.repository;


import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.data.entity.Product;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface ProductRepository {
    public List<Product> getProducts();
    public void createProduct(Product p);
    public List<Product> getMachineProducts(String machineId);
    public Product getProduct(String id);
	public List<Product> getProducts(String machineId, ProductType simple);
}
