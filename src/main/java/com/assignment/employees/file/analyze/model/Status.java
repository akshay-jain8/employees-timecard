package com.assignment.employees.file.analyze.model;

public enum Status {
    ACTIVE,
    INACTIVE;


    public static Status getStatus(String name) {
        if (name.equals("Active")) {
            return Status.ACTIVE;
        }
        else {
            return Status.INACTIVE;
        }
    }
}
