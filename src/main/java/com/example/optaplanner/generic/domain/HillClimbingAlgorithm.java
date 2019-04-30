package com.example.optaplanner.generic.domain;

import org.optaplanner.core.config.localsearch.LocalSearchType;

public class HillClimbingAlgorithm extends LocalSearchAlgorithm{



    public HillClimbingAlgorithm() {
    }

    public HillClimbingAlgorithm(LocalSearchType localSearchType) {
        super(localSearchType);
    }
}
