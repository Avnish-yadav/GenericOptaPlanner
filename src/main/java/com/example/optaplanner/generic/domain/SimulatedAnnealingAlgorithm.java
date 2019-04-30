package com.example.optaplanner.generic.domain;

import org.optaplanner.core.config.localsearch.LocalSearchType;

public class SimulatedAnnealingAlgorithm extends LocalSearchAlgorithm {

    private String startingTemperature;
    private int entityTabuSize;


    public SimulatedAnnealingAlgorithm() {
    }

    public SimulatedAnnealingAlgorithm(String startingTemperature, int entityTabuSize, LocalSearchType localSearchType) {
        super(localSearchType);
        this.startingTemperature = startingTemperature;
        this.entityTabuSize = entityTabuSize;

    }

    public int getEntityTabuSize() {
        return entityTabuSize;
    }

    public void setEntityTabuSize(int entityTabuSize) {
        this.entityTabuSize = entityTabuSize;
    }

    public String getStartingTemperature() {
        return startingTemperature;
    }

    public void setStartingTemperature(String startingTemperature) {
        this.startingTemperature = startingTemperature;
    }
}
