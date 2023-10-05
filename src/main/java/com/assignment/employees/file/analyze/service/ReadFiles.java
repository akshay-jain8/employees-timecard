package com.assignment.employees.file.analyze.service;

import com.assignment.employees.file.analyze.model.Employee;
import com.assignment.employees.file.analyze.model.Status;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ReadFiles {

    private static final Set<String> positionIdSet = new HashSet<>();
    private static final List<Employee> employeeList = new ArrayList<>();


    /*
        Adding Employees details from Assignment_Timecard.txt file to employeeList
     */
    public void addEmployeeDetails(String location) {
        String line;
        String[] columns;
        String positionId;
        Status status;
        LocalDateTime time = null;
        LocalDateTime timeOut = null;
        LocalTime timecard = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        String employeeName;
        String fileNumber;

        try (FileInputStream inputStream = new FileInputStream(location);
             InputStreamReader inputReader = new InputStreamReader(inputStream);
             LineNumberReader lineReader = new LineNumberReader(inputReader)) {

            lineReader.readLine();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a", Locale.ENGLISH);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH);
            while ((line = lineReader.readLine()) != null) {
                columns = line.split("  ");
                positionId = columns[0];
                status = Status.getStatus(columns[1]);
                if(!columns[2].isEmpty()) {
                    time = LocalDateTime.parse(columns[2], dateTimeFormatter);    
                }
                if (!columns[3].isEmpty()) {
                    timeOut = LocalDateTime.parse(columns[3], dateTimeFormatter);   
                }
                if (!columns[4].isEmpty()) {
                    timecard = LocalTime.parse(columns[4], timeFormatter);   
                }
                if (!columns[5].isEmpty()) {
                    startDate = LocalDate.parse(columns[5], dateFormatter);   
                }
                if (!columns[6].isEmpty()) {
                    endDate = LocalDate.parse(columns[6], dateFormatter);   
                }
                employeeName = columns[7];
                fileNumber = columns[8];
                positionIdSet.add(positionId);
                employeeList.add(new Employee(positionId, employeeName, status, time, timeOut, timecard, startDate, endDate, fileNumber));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<String> getPositionIdSet() {
        return positionIdSet;
    }


    /*
        Print the details of Employees who has worked for seven consecutive days in console
        as well as save this details in output.txt file
     */
    public List<String> printEmpWhoWorkedForSevenConsecutiveDays(List<Employee> employee) {
        List<String> output = new ArrayList<>();
        Set<LocalDate> workingDays = getWorkingDays(employee);
        Optional<LocalDate> startDate = workingDays.stream().filter(Objects::nonNull).min(LocalDate::compareTo);
        LocalDate date = null;
        if (startDate.isPresent()) {
            date = startDate.get();
        }
        List<LocalDate> consecutiveDays = getConsecutiveDays(date);
        if (workingDays.containsAll(consecutiveDays)) {
            output.add("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
            System.out.println("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
        }
        return output;
    }

    /*
        Print the details of Employees who have less than 10 hours of time between shifts but greater than 1 hour
        as well as save this details in output.txt file
     */
    public List<String> printEmpBasedOnTimeBetweenShift(List<Employee> employee) {
        List<String> output = new ArrayList<>();
        boolean isTimeBetweenShiftConditionMatch = getTimeBetweenShifts(employee);
        if (isTimeBetweenShiftConditionMatch) {
            output.add("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
            System.out.println("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
        }
        return output;
    }

    /*
        Print the details of Employees who has worked for more than 14 hours in a single shift
        as well as save this details in output.txt file
     */
    public List<String> printEmpBasedOnHoursInSingleShift(List<Employee> employee) {
        List<String> output = new ArrayList<>();
        boolean isHourInSingleShiftConditionMatch = getNumberOfHoursInSingleShift(employee);
        if (isHourInSingleShiftConditionMatch) {
            output.add("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
            System.out.println("Position Id: " + employee.get(0).getPositionId() + ", Employee Name: " + employee.get(0).getName());
        }
        return output;
    }

    /*
        Finding all records of specific employee with the help of positionId
     */
    public List<Employee> getEmployee(String positionId) {
        List<Employee> employee = new ArrayList<>();
        for (Employee emp : employeeList) {
            if (emp.getPositionId().equals(positionId)) {
                employee.add(emp);
            }
        }
        return employee;
    }

  /*
    Finding all days of working
   */
    private static Set<LocalDate> getWorkingDays(List<Employee> employee) {
        Set<LocalDate> workingDays = new HashSet<>();
        for(Employee emp : employee) {
            if(emp.getTime() != null) {
                workingDays.add(emp.getTime().toLocalDate());
            }
        }
        return workingDays;
    }


    /*
        Assumption: Taking consecutive days from the starting of working date
     */
    private static List<LocalDate> getConsecutiveDays(LocalDate date) {
        List<LocalDate> consecutiveDays = new ArrayList<>();
        for (int i = 1; i<7; i++) {
            if (date != null) {
                consecutiveDays.add(date.plusDays(i));
            }
        }
        return consecutiveDays;
    }

    /*
        Iterating over every working shift
        until time difference between shift is
        greater than 1 and less than 10 is found
     */
    private static boolean getTimeBetweenShifts(List<Employee> employee) {
        for (int i = 0; i < employee.size()-1; i = i+2) {
            LocalDateTime t1 = employee.get(i).getTimeOut();
            LocalDateTime t2 = employee.get(i+1).getTime();
            int difference = t2.getHour() - t1.getHour();
            if (difference > 1 && difference < 10) {
                return true;
            }
        }
        return false;
    }

    /*
        Iterating over every employee
        to find all who has worked for
        more than 14 hours in a single shift
     */
    private static boolean getNumberOfHoursInSingleShift(List<Employee> employee) {
        for (Employee emp : employee) {
            if (emp.getTimeCard() != null && emp.getTimeCard().getHour() >14) {
                return true;
            }
        }
        return false;
    }
}
