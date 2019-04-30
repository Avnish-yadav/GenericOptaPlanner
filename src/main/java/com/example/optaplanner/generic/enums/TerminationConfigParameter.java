package com.example.optaplanner.generic.enums;

public enum TerminationConfigParameter {
    DAY_SPENT_LIMIT("Day Spent Limit"),
    HOUR_SPENT_LIMIT("Hour Spent Limit"),
    MINUTE_SPENT_LIMIT("Minute Spent Limit"),
    SECOND_SPENT_LIMIT("Second Spent Limit"),
    MILLI_SECOND_SPENT_LIMIT("Millisecond Spent Limit");

    TerminationConfigParameter(String value) {
    }

    private String value;

    public static TerminationConfigParameter valueOfTermination(String value) {
        for (TerminationConfigParameter parameter : values()) {
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
