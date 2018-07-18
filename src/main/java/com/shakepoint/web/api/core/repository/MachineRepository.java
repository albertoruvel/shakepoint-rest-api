package com.shakepoint.web.api.core.repository;


import com.shakepoint.web.api.data.entity.VendingConnection;
import com.shakepoint.web.api.data.entity.VendingMachine;
import com.shakepoint.web.api.data.entity.VendingMachineProductStatus;

import java.util.List;

public interface MachineRepository {
    public boolean containProduct(String machineId, String productId);

    public void addMachine(VendingMachine machine);

    public List<VendingMachine> getMachines();

    public VendingMachine getMachine(String machineId);

    public int getAlertedProducts(String machineId);

    public void updateMachine(VendingMachine machine);

    public void addMachineProduct(VendingMachineProductStatus mp);

    public VendingMachineProductStatus getMachineProduct(String id);

    public List<VendingMachineProductStatus> getMachineProducts(String machineId);

    public void deleteMachineProduct(String id);

    public List<VendingMachine> getAlertedMachines(String technicianId);

    public List<VendingMachine> getFailedMachines(String technicianId);

    public int getActiveMachines();

    public int getAlertedMachines();

    public int getAlertedMachinesNumber(String technicianId);

    public List<VendingMachine> getTechnicianMachines(String id);

    public boolean isMachineAlerted(String id);

    public List<VendingMachine> searchByName(String machineName);

    public VendingConnection getVendingConnection(String id);
}
