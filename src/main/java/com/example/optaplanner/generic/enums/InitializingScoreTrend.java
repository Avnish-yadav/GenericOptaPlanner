package com.example.optaplanner.generic.enums;

public enum InitializingScoreTrend {
    ANY("Any"),
    ONLY_UP("Only Up"),
    ONLY_DOWN("Only Down");

    InitializingScoreTrend(String value) {
    }

    private String value;

    public static InitializingScoreTrend valueOfTrend(String value) {
        for (InitializingScoreTrend parameter : values()) {
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
