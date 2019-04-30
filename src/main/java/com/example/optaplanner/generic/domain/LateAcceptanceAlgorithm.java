package com.example.optaplanner.generic.domain;

import org.optaplanner.core.config.localsearch.LocalSearchType;

public class LateAcceptanceAlgorithm extends LocalSearchAlgorithm {

    private int entityTabuSize;
    private int lateAcceptanceSize;


    public LateAcceptanceAlgorithm() {
    }

    public LateAcceptanceAlgorithm(int lateAcceptanceSize, int entityTabuSize, LocalSearchType localSearchType) {
        super(localSearchType);
        this.lateAcceptanceSize = lateAcceptanceSize;
        this.entityTabuSize = entityTabuSize;

    }

    public int getEntityTabuSize() {
        return entityTabuSize;
    }

    public void setEntityTabuSize(int entityTabuSize) {
        this.entityTabuSize = entityTabuSize;
    }

    public int getLateAcceptanceSize() {
        return lateAcceptanceSize;
    }

    public void setLateAcceptanceSize(int lateAcceptanceSize) {
        this.lateAcceptanceSize = lateAcceptanceSize;
    }

    public LateAcceptanceAlgorithm(LocalSearchType localSearchType) {
        super(localSearchType);
    }
}
