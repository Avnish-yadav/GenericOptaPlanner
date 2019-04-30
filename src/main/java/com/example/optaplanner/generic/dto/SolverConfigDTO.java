package com.example.optaplanner.generic.dto;

import com.example.optaplanner.generic.domain.LocalSearchAlgorithm;
import com.example.optaplanner.generic.enums.InitializingScoreTrend;
import com.example.optaplanner.generic.enums.ScoreCalculatorType;
import com.example.optaplanner.generic.enums.TerminationConfigParameter;
import org.optaplanner.core.config.constructionheuristic.ConstructionHeuristicType;
import org.optaplanner.core.config.localsearch.LocalSearchType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SolverConfigDTO {

    TerminationConfigParameter terminationConfigParameter;

    int terminationConfigParameterValue;

    InitializingScoreTrend initializingScoreTrend;

    ScoreCalculatorType scoreCalculatorType;

    String scoreCalculatorClassName;

    List<String> scoreCalculatorDroolFile = new ArrayList<>();

    ConstructionHeuristicType constructionHeuristicType;

    LocalSearchAlgorithm localSearchAlgorithm;

    int lateAcceptance;

    int acceptedCountLimit;

    public SolverConfigDTO() {
    }

    public TerminationConfigParameter getTerminationConfigParameter() {
        return terminationConfigParameter;
    }

    public void setTerminationConfigParameter(TerminationConfigParameter terminationConfigParameter) {
        this.terminationConfigParameter = terminationConfigParameter;
    }

    public int getTerminationConfigParameterValue() {
        return terminationConfigParameterValue;
    }

    public void setTerminationConfigParameterValue(int terminationConfigParameterValue) {
        this.terminationConfigParameterValue = terminationConfigParameterValue;
    }

    public InitializingScoreTrend getInitializingScoreTrend() {
        return initializingScoreTrend;
    }

    public void setInitializingScoreTrend(InitializingScoreTrend initializingScoreTrend) {
        this.initializingScoreTrend = initializingScoreTrend;
    }

    public ScoreCalculatorType getScoreCalculatorType() {
        return scoreCalculatorType;
    }

    public void setScoreCalculatorType(ScoreCalculatorType scoreCalculatorType) {
        this.scoreCalculatorType = scoreCalculatorType;
    }

    public String getScoreCalculatorClassName() {
        return scoreCalculatorClassName;
    }

    public void setScoreCalculatorClassName(String scoreCalculatorClassName) {
        this.scoreCalculatorClassName = scoreCalculatorClassName;
    }

    public List<String> getScoreCalculatorDroolFile() {
        return scoreCalculatorDroolFile;
    }

    public void setScoreCalculatorDroolFile(List<String> scoreCalculatorDroolFile) {
        this.scoreCalculatorDroolFile = scoreCalculatorDroolFile;
    }

    public ConstructionHeuristicType getConstructionHeuristicType() {
        return constructionHeuristicType;
    }

    public void setConstructionHeuristicType(ConstructionHeuristicType constructionHeuristicType) {
        this.constructionHeuristicType = constructionHeuristicType;
    }

    public LocalSearchAlgorithm getLocalSearchAlgorithm() {
        return localSearchAlgorithm;
    }

    public void setLocalSearchAlgorithm(LocalSearchAlgorithm localSearchAlgorithm) {
        this.localSearchAlgorithm = localSearchAlgorithm;
    }

    public int getLateAcceptance() {
        return lateAcceptance;
    }

    public void setLateAcceptance(int lateAcceptance) {
        this.lateAcceptance = lateAcceptance;
    }

    public int getAcceptedCountLimit() {
        return acceptedCountLimit;
    }

    public void setAcceptedCountLimit(int acceptedCountLimit) {
        this.acceptedCountLimit = acceptedCountLimit;
    }
}
