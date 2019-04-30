package com.example.optaplanner.generic.domain;


import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Lecture {

    private Integer roomNumber;
    private Integer period;

    @PlanningVariable(valueRangeProviderRefs = {"periodAvailable"})
    public Integer getRoomNumber() {
        return roomNumber;
    }


    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    @PlanningVariable(valueRangeProviderRefs = {"roomAvailable"})
    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
