/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.api.core.repository;


import com.shakepoint.web.api.core.machine.ProductType;
import com.shakepoint.web.api.data.entity.Flavor;
import com.shakepoint.web.api.data.entity.Product;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;

import java.util.List;

public interface ProductRepository {
    public List<Product> getProducts();
    public void createProduct(Product p);
    public List<VendingMachineProductStatus> getMachineProducts(String machineId);
    public Product getProduct(String id);
	public List<Product> getProducts(String machineId, ProductType simple);

    public void createNewFlavor(Flavor flavor);

    public List<Flavor> getFlavors();
}
