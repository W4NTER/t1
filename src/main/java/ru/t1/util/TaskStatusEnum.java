package ru.t1.util;

public enum TaskStatusEnum {
    TODO("TODO"),
    IN_WORK("IN_WORK"),
    THIS_WEEK("THIS_WEEK"),
    IN_PROGRESS("THIS_WEEK"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS");


    private final String value;

    TaskStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
