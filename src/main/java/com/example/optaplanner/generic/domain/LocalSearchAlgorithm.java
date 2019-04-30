package com.example.optaplanner.generic.domain;

import org.optaplanner.core.config.localsearch.LocalSearchType;

public class LocalSearchAlgorithm {


    LocalSearchType localSearchType;

    public LocalSearchType getLocalSearchType() {
        return localSearchType;
    }

    public void setLocalSearchType(LocalSearchType localSearchType) {
        this.localSearchType = localSearchType;
    }

    public LocalSearchAlgorithm() {
    }

    public LocalSearchAlgorithm(LocalSearchType localSearchType) {
        this.localSearchType = localSearchType;
    }
}
