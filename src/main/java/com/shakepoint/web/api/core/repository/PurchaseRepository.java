package com.shakepoint.web.api.core.repository;

import com.shakepoint.web.api.data.entity.Purchase;
import com.shakepoint.web.api.data.entity.VendingMachine;

import java.util.List;
import java.util.Map;

public interface PurchaseRepository {
	public Purchase getPurchase(String purchaseId);

	public double getTodayTotalPurchases();
	public Map<String, List<Double>> getPerMachineValues(String[] range, List<VendingMachine> machines);
	public Map<String, List<Double>> getPerMachineProductsCountValues(String[] range, List<VendingMachine> machines);
	public List<Double> getTotalIncomeValues(String[] range);
	public List<Double> getTotalIncomeValues(String[] range, String machineId);
	public List<Purchase> getAuthorizedPurchases(String userId, String machineId);
	public List<Purchase> getUserPurchases(String userId);
	public void update(Purchase purchase);
	public Purchase getAvailablePurchaseForMachine(String productId, String machineId, Integer slot);

	public Integer getProductCountForDateRange(String id, String[] range, String machineId);
}
