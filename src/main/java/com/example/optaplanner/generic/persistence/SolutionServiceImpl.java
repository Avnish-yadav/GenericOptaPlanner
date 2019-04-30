/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.optaplanner.generic.persistence;

import static com.example.optaplanner.generic.constants.AppConstants.*;
import com.example.optaplanner.generic.domain.Customer;
import com.example.optaplanner.generic.domain.Depot;
import com.example.optaplanner.generic.domain.Vehicle;
import com.example.optaplanner.generic.domain.VehicleRoutingSolution;
import com.example.optaplanner.generic.domain.location.AirLocation;
import com.example.optaplanner.generic.domain.location.Location;
import com.example.optaplanner.generic.domain.timewindowed.TimeWindowedCustomer;
import com.example.optaplanner.generic.domain.timewindowed.TimeWindowedDepot;
import com.example.optaplanner.generic.domain.timewindowed.TimeWindowedVehicleRoutingSolution;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.impl.solver.random.RandomUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class SolutionServiceImpl extends SolutionService<VehicleRoutingSolution> {


    private Random demandRandom = new Random(37);
    private Long nextLocationId = null;
    private Long minimumTimeWindowTime = null;
    private Long maximumTimeWindowTime = null;
    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#,##0.00");
    public SolutionServiceImpl() {

    }

    @Override
    public boolean isWrapInScrollPane() {
        return false;
    }

    @Override
    public void resetPanel(VehicleRoutingSolution solution) {
        reset(solution);
        resetNextLocationId();
    }

    private void resetNextLocationId() {
        long highestLocationId = 0L;
        for (Location location : getSolution().getLocationList()) {
            if (highestLocationId < location.getId().longValue()) {
                highestLocationId = location.getId();
            }
        }
        nextLocationId = highestLocationId + 1L;
    }

    @Override
    public void updatePanel(VehicleRoutingSolution solution) {
        updatePanel(solution);
    }

    public SolverAndPersistence getWorkflowFrame() {
        return solverAndPersistence;
    }

    public void insertLocationAndCustomer(double longitude, double latitude) {
        final Location newLocation;
        switch (getSolution().getDistanceType()) {
            case AIR_DISTANCE:
                newLocation = new AirLocation();
                break;
            case ROAD_DISTANCE:
                logger.warn("Adding locations for a road distance dataset is not supported.");
                return;
            case SEGMENTED_ROAD_DISTANCE:
                logger.warn("Adding locations for a segmented road distance dataset is not supported.");
                return;
            default:
                throw new IllegalStateException("The distanceType (" + getSolution().getDistanceType()
                        + ") is not implemented.");
        }
        newLocation.setId(nextLocationId);
        nextLocationId++;
        newLocation.setLongitude(longitude);
        newLocation.setLatitude(latitude);
        logger.info("Scheduling insertion of newLocation ({}).", newLocation);
    }

    protected Customer createCustomer(VehicleRoutingSolution solution, Location newLocation) {
        Customer newCustomer;
        if (solution instanceof TimeWindowedVehicleRoutingSolution) {
            TimeWindowedCustomer newTimeWindowedCustomer = new TimeWindowedCustomer();
            TimeWindowedDepot timeWindowedDepot = (TimeWindowedDepot) solution.getDepotList().get(0);
            long windowTime = (timeWindowedDepot.getDueTime() - timeWindowedDepot.getReadyTime()) / 4L;
            long readyTime = RandomUtils.nextLong(demandRandom, windowTime * 3L);
            newTimeWindowedCustomer.setReadyTime(readyTime);
            newTimeWindowedCustomer.setDueTime(readyTime + windowTime);
            newTimeWindowedCustomer.setServiceDuration(Math.min(10000L, windowTime / 2L));
            newCustomer = newTimeWindowedCustomer;
        } else {
            newCustomer = new Customer();
        }
        newCustomer.setId(newLocation.getId());
        newCustomer.setLocation(newLocation);
        // Demand must not be 0
        newCustomer.setDemand(demandRandom.nextInt(10) + 1);
        return newCustomer;
    }

    public void reset(VehicleRoutingSolution solution) {
        for (Location location : solution.getLocationList()) {
        }
        determineMinimumAndMaximumTimeWindowTime(solution);

        for (Customer customer : solution.getCustomerList()) {

            if (customer instanceof TimeWindowedCustomer) {
                TimeWindowedCustomer timeWindowedCustomer = (TimeWindowedCustomer) customer;
            }

        }

        for (Depot depot : solution.getDepotList()) {
        }
        int colorIndex = 0;
        // TODO Too many nested for loops
        Map<String, List<Map<String, Object>>> data = new HashMap<>();
        for (Vehicle vehicle : solution.getVehicleList()) {
            List<Map<String, Object>> customerList = new ArrayList<>();
            Customer vehicleInfoCustomer = null;
            long longestNonDepotDistance = -1L;
            int load = 0;
            for (Customer customer : solution.getCustomerList()) {
                if (customer.getPreviousStandstill() != null && customer.getVehicle() == vehicle) {

                    load += customer.getDemand();
                    Location previousLocation = customer.getPreviousStandstill().getLocation();
                    Location location = customer.getLocation();
                    // Determine where to draw the vehicle info
                    long distance = customer.getDistanceFromPreviousStandstill();
                    if (customer.getPreviousStandstill() instanceof Customer) {
                        if (longestNonDepotDistance < distance) {
                            longestNonDepotDistance = distance;
                            vehicleInfoCustomer = customer;
                        }
                    } else if (vehicleInfoCustomer == null) {
                        // If there is only 1 customer in this chain, draw it on a line to the Depot anyway
                        vehicleInfoCustomer = customer;
                    }
                    // Line back to the vehicle depot
                    if (customer.getNextCustomer() == null) {
                        Location vehicleLocation = vehicle.getLocation();

                    }
                    Map<String, Object> customerData = new HashMap<>();
                    customerData.put(CUSTOMER_DEMAND, customer.getDemand());
                    customerData.put(CUSTOMER_LATITUDE, customer.getLocation().getLatitude());
                    customerData.put(CUSTOMER_LONGITUDE, customer.getLocation().getLongitude());
                    customerData.put(VEHICLE_DEPOT_LATITUDE, vehicle.getDepot().getLocation().getLatitude());
                    customerData.put(VEHICLE_DEPOT_LONGITUDE, vehicle.getDepot().getLocation().getLongitude());
                    customerData.put(NEXT_CUSTOMER_LATITUDE, vehicle.getNextCustomer().getLocation().getLatitude());
                    customerData.put(NEXT_CUSTOMER_LONGITUDE, vehicle.getNextCustomer().getLocation().getLongitude());
                    customerData.put(VEHICLE, vehicle.toString());
                    customerList.add(customerData);
                }
            }

            // Draw vehicle info
            if (vehicleInfoCustomer != null) {
                if (load > vehicle.getCapacity()) {

                }
                Location previousLocation = vehicleInfoCustomer.getPreviousStandstill().getLocation();
                Location location = vehicleInfoCustomer.getLocation();

                boolean ascending = (previousLocation.getLongitude() < location.getLongitude())
                        ^ (previousLocation.getLatitude() < location.getLatitude());


            }
            data.put(vehicle.toString(), customerList);
        }
        System.out.println("CDT=="+data);
        System.out.println("Vehicle List=="+solution.getVehicleList());
        // Legend

        String vehiclesSizeString = solution.getVehicleList().size() + " vehicles";
        String customersSizeString = solution.getCustomerList().size() + " customers";

        // Show soft score
        HardSoftLongScore score = solution.getScore();
        if (score != null) {
            String distanceString;
            if (!score.isFeasible()) {
                distanceString = "Not feasible";
            } else {
                distanceString = solution.getDistanceString(NUMBER_FORMAT);
            }

        }
    }

    private void determineMinimumAndMaximumTimeWindowTime(VehicleRoutingSolution solution) {
        minimumTimeWindowTime = Long.MAX_VALUE;
        maximumTimeWindowTime = Long.MIN_VALUE;
        for (Depot depot : solution.getDepotList()) {
            if (depot instanceof TimeWindowedDepot) {
                TimeWindowedDepot timeWindowedDepot = (TimeWindowedDepot) depot;
                long readyTime = timeWindowedDepot.getReadyTime();
                if (readyTime < minimumTimeWindowTime) {
                    minimumTimeWindowTime = readyTime;
                }
                long dueTime = timeWindowedDepot.getDueTime();
                if (dueTime > maximumTimeWindowTime) {
                    maximumTimeWindowTime = dueTime;
                }
            }
        }
        for (Customer customer : solution.getCustomerList()) {
            if (customer instanceof TimeWindowedCustomer) {
                TimeWindowedCustomer timeWindowedCustomer = (TimeWindowedCustomer) customer;
                long readyTime = timeWindowedCustomer.getReadyTime();
                if (readyTime < minimumTimeWindowTime) {
                    minimumTimeWindowTime = readyTime;
                }
                long dueTime = timeWindowedCustomer.getDueTime();
                if (dueTime > maximumTimeWindowTime) {
                    maximumTimeWindowTime = dueTime;
                }
            }
        }
    }

    private int calculateTimeWindowDegree(long timeWindowTime) {
        return (int) (360L * (timeWindowTime - minimumTimeWindowTime) / (maximumTimeWindowTime - minimumTimeWindowTime));
    }

}
