package com.assignment.employees.file.analyze.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Employee {
    private String positionId;
    private String name;
    private Status status;
    private LocalDateTime time;
    private LocalDateTime timeOut;
    private LocalTime timeCard;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fileNumber;
}
