package com.example.optaplanner.generic.solver.score;

import com.example.optaplanner.generic.domain.Customer;
import com.example.optaplanner.generic.domain.Standstill;
import com.example.optaplanner.generic.domain.Vehicle;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import com.example.optaplanner.generic.domain.timewindowed.TimeWindowedCustomer;
import com.example.optaplanner.generic.domain.timewindowed.TimeWindowedVehicleRoutingSolution;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleRoutingEasyScoreCalculator implements EasyScoreCalculator<VehicleRoutingSolution> {

    @Override
    public HardSoftLongScore calculateScore(VehicleRoutingSolution solution) {
        boolean timeWindowed = solution instanceof TimeWindowedVehicleRoutingSolution;
        List<Customer> customerList = solution.getCustomerList();
        List<Vehicle> vehicleList = solution.getVehicleList();
        Map<Vehicle, Integer> vehicleDemandMap = new HashMap<>(vehicleList.size());
        for (Vehicle vehicle : vehicleList) {
            vehicleDemandMap.put(vehicle, 0);
        }
        long hardScore = 0L;
        long softScore = 0L;
        for (Customer customer : customerList) {
            Standstill previousStandstill = customer.getPreviousStandstill();
            if (previousStandstill != null) {
                Vehicle vehicle = customer.getVehicle();
                vehicleDemandMap.put(vehicle, vehicleDemandMap.get(vehicle) + customer.getDemand());
                // Score constraint distanceToPreviousStandstill
                softScore -= customer.getDistanceFromPreviousStandstill();
                if (customer.getNextCustomer() == null) {
                    // Score constraint distanceFromLastCustomerToDepot
                    softScore -= customer.getLocation().getDistanceTo(vehicle.getLocation());
                }
                if (timeWindowed) {
                    TimeWindowedCustomer timeWindowedCustomer = (TimeWindowedCustomer) customer;
                    long dueTime = timeWindowedCustomer.getDueTime();
                    Long arrivalTime = timeWindowedCustomer.getArrivalTime();
                    if (dueTime < arrivalTime) {
                        // Score constraint arrivalAfterDueTime
                        hardScore -= (arrivalTime - dueTime);
                    }
                }
            }
        }
        for (Map.Entry<Vehicle, Integer> entry : vehicleDemandMap.entrySet()) {
            int capacity = entry.getKey().getCapacity();
            int demand = entry.getValue();
            if (demand > capacity) {
                // Score constraint vehicleCapacity
                hardScore -= (demand - capacity);
            }
        }
        // Score constraint arrivalAfterDueTimeAtDepot is a built-in hard constraint in VehicleRoutingImporter
        return HardSoftLongScore.valueOf(hardScore, softScore);
    }

}
