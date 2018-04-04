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
	public List<Purchase> getAuthorizedPurchases(String userId, String machineId, int pageNumber);
	public List<Purchase> getUserPurchases(String userId, int pageNumber);
	public void update(Purchase purchase);
	public List<Purchase> getAvailablePurchasesForMachine(String productId, String machineId);

	public Integer getProductCountForDateRange(String id, String[] range, String machineId);
}
