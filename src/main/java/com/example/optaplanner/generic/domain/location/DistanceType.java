package com.example.optaplanner.generic.domain.location;


public enum DistanceType {
    /**
     * Requires that all {@link Location} instances are of type {@link AirLocation}.
     */
    AIR_DISTANCE,
    /**
     * Requires that all {@link Location} instances are of type {@link RoadLocation}.
     */
    ROAD_DISTANCE,
    /**
     * Requires that all {@link Location} instances are of type {@link com.example.optaplanner.generic.domain.location.segmented.RoadSegmentLocation} or {@link com.example.optaplanner.generic.domain.location.segmented.HubSegmentLocation}.
     */
    SEGMENTED_ROAD_DISTANCE;
}
