package com.example.optaplanner.generic.domain;

import org.optaplanner.core.config.localsearch.LocalSearchType;

public class TabuSearchAlgorithm extends LocalSearchAlgorithm {

    private int entityTabuSize;
    private int valueTabuSize;
    private int moveTabuSize;
    private int undoMoveTabuSize;

    public TabuSearchAlgorithm() {
    }

    public TabuSearchAlgorithm(int entityTabuSize, int valueTabuSize, int moveTabuSize, int undoMoveTabuSize, LocalSearchType localSearchType) {
        super(localSearchType);
        this.entityTabuSize = entityTabuSize;
        this.valueTabuSize = valueTabuSize;
        this.moveTabuSize = moveTabuSize;
        this.undoMoveTabuSize = undoMoveTabuSize;

    }

    public int getEntityTabuSize() {
        return entityTabuSize;
    }

    public void setEntityTabuSize(int entityTabuSize) {
        this.entityTabuSize = entityTabuSize;
    }

    public int getValueTabuSize() {
        return valueTabuSize;
    }

    public void setValueTabuSize(int valueTabuSize) {
        this.valueTabuSize = valueTabuSize;
    }

    public int getMoveTabuSize() {
        return moveTabuSize;
    }

    public void setMoveTabuSize(int moveTabuSize) {
        this.moveTabuSize = moveTabuSize;
    }

    public int getUndoMoveTabuSize() {
        return undoMoveTabuSize;
    }

    public void setUndoMoveTabuSize(int undoMoveTabuSize) {
        this.undoMoveTabuSize = undoMoveTabuSize;
    }
}
