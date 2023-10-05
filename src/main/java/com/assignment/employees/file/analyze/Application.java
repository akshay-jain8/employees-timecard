package com.assignment.employees.file.analyze;

import com.assignment.employees.file.analyze.model.Employee;
import com.assignment.employees.file.analyze.service.ReadFiles;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

@SpringBootApplication
public class Application {
	private static final String INPUT_FILE = "src/main/resources/Assignment_Timecard.txt";
	private static final String OUTPUT_FILE = "src/main/resources/output.txt";

	public static void main(String[] args) {
		ReadFiles readFiles = new ReadFiles();
		readFiles.addEmployeeDetails(INPUT_FILE);

		try(FileOutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
			OutputStreamWriter outputWriter = new OutputStreamWriter(outputStream)) {

			System.out.println("Employee who has worked for seven consecutive days:");
			outputWriter.write("Employee who has worked for seven consecutive days:");
			for (String positionId : readFiles.getPositionIdSet()) {
				List<Employee> employee = readFiles.getEmployee(positionId);
				List<String> output = readFiles.printEmpWhoWorkedForSevenConsecutiveDays(employee);
				for (String text : output) {
					outputWriter.write("\n"+text);
				}
			}

			System.out.println("\nEmployee who have less than 10 hours of time between shifts but greater than 1 hour:");
			outputWriter.write("\n\nEmployee who have less than 10 hours of time between shifts but greater than 1 hour:");
			for (String positionId : readFiles.getPositionIdSet()) {
				List<Employee> employee = readFiles.getEmployee(positionId);
				List<String> output = readFiles.printEmpBasedOnTimeBetweenShift(employee);
				for (String text : output) {
					outputWriter.write("\n" + text);
				}
			}

			System.out.println("\nEmployee who has worked for more than 14 hours in a single shift:");
			outputWriter.write("\n\nEmployee who has worked for more than 14 hours in a single shift:");
			for (String positionId : readFiles.getPositionIdSet()) {
				List<Employee> employee = readFiles.getEmployee(positionId);
				List<String> output = readFiles.printEmpBasedOnHoursInSingleShift(employee);
				for (String text : output) {
					outputWriter.write("\n" + text);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
