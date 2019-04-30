package com.example.optaplanner.generic.domain.solver.nearby;

import com.example.optaplanner.generic.domain.Customer;
import com.example.optaplanner.generic.domain.Standstill;
import org.optaplanner.core.impl.heuristic.selector.common.nearby.NearbyDistanceMeter;

public class CustomerNearbyDistanceMeter implements NearbyDistanceMeter<Customer, Standstill> {

    @Override
    public double getNearbyDistance(Customer origin, Standstill destination) {
        long distance = origin.getDistanceTo(destination);
        // If arriving early also inflicts a cost (more than just not using the vehicle more), such as the driver's wage, use this:
//        if (origin instanceof TimeWindowedCustomer && destination instanceof TimeWindowedCustomer) {
//            distance += ((TimeWindowedCustomer) origin).getTimeWindowGapTo((TimeWindowedCustomer) destination);
//        }
        return distance;
    }

}
