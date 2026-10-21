
package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    static App app;

    @BeforeAll
    static void init() {
        app = new App();
    }

    @Test
    void printSalariesTestNull() {
        app.printSalaries(null);
    }

    @Test
    void printSalariesTestNullDepartment() {
        app.printSalaries(null);
    }

    @Test
    void displayEmployeeTestNull() {
        app.displayEmployee(null);
    }

    @Test
    void displayEmployeeTestNormal() {
        Employee emp = new Employee();
        emp.emp_no = 12345;
        emp.first_name = "John";
        emp.last_name = "Smith";
        emp.title = "Engineer";
        emp.salary = 50000;

        app.displayEmployee(emp);
    }
}
