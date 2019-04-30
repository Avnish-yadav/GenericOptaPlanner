package com.example.optaplanner.generic.enums;

public enum ScoreCalculatorType {
    EASY_SCORE_CALCULATOR("Easy Score Calculator"),
    INCREMENTAL_SCORE_CALCULATOR("Incremental Score Calculator"),
    DROOL_SCORE_CALCULATOR("Drool Score Calculator");

    ScoreCalculatorType(String value) {
    }

    private String value;


    public static ScoreCalculatorType valueOfScoreCalculator(String value) {
        for (ScoreCalculatorType parameter : values()) {
            if (value.equalsIgnoreCase(parameter.getValue())) {
                return parameter;
            }
        }
        return null;
    }


    public String getValue() {
        return value;
    }
}
