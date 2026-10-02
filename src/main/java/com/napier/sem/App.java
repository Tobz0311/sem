package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");

            try {
                Thread.sleep(30000);

                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:33060/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println(
                        "Failed to connect to database attempt "
                                + Integer.toString(i)
                );
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Get an employee from the database.
     */
    public Employee getEmployee(int ID) {
        try {
            Statement stmt = con.createStatement();

            String strSelect =
                    "SELECT e.emp_no, e.first_name, e.last_name, "
                            + "t.title, s.salary, d.dept_name, "
                            + "m.first_name AS manager_first_name, "
                            + "m.last_name AS manager_last_name "
                            + "FROM employees e "
                            + "LEFT JOIN titles t ON e.emp_no = t.emp_no "
                            + "LEFT JOIN salaries s ON e.emp_no = s.emp_no "
                            + "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no "
                            + "LEFT JOIN departments d ON de.dept_no = d.dept_no "
                            + "LEFT JOIN dept_manager dm ON de.dept_no = dm.dept_no "
                            + "LEFT JOIN employees m ON dm.emp_no = m.emp_no "
                            + "WHERE e.emp_no = " + ID
                            + " AND t.to_date = '9999-01-01' "
                            + "AND s.to_date = '9999-01-01' "
                            + "AND de.to_date = '9999-01-01' "
                            + "AND dm.to_date = '9999-01-01'";

            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                emp.dept_name = rset.getString("dept_name");

                emp.manager =
                        rset.getString("manager_first_name")
                                + " "
                                + rset.getString("manager_last_name");

                return emp;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public ArrayList<Employee> getAllSalaries() {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        try {
            Statement stmt = con.createStatement();
            //noinspection SqlResolve
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, "
                            + "employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no "
                            + "AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";

            ResultSet rset = stmt.executeQuery(strSelect);
            while (rset.next()) {
                Employee emp = new Employee();

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");

                employees.add(emp);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return employees;
    }

    public void printSalaries(ArrayList<Employee> employees) {
        System.out.println(
                String.format("%-10s %-15s %-15s %-10s",
                        "Emp No", "First Name", "Last Name", "Salary")
        );

        for (Employee emp : employees) {
            System.out.println(
                    String.format("%-10d %-15s %-15s %-10d",
                            emp.emp_no,
                            emp.first_name,
                            emp.last_name,
                            emp.salary)
            );
        }
    }

    /**
     * Get salaries for employees with a given role.
     * public void getSalariesByRole(String role)
     * {
     * try
     * {
     * Statement stmt = con.createStatement();
     * <p>
     * String strSelect =
     * "SELECT employees.emp_no, employees.first_name, "
     * + "employees.last_name, salaries.salary "
     * + "FROM employees, salaries, titles "
     * + "WHERE employees.emp_no = salaries.emp_no "
     * + "AND employees.emp_no = titles.emp_no "
     * + "AND salaries.to_date = '9999-01-01' "
     * + "AND titles.to_date = '9999-01-01' "
     * + "AND titles.title = '" + role + "' "
     * + "ORDER BY employees.emp_no ASC";
     * <p>
     * ResultSet rset = stmt.executeQuery(strSelect);
     * <p>
     * System.out.println("Employees with role: " + role);
     * System.out.println();
     * <p>
     * while (rset.next())
     * {
     * System.out.println(
     * rset.getInt("emp_no") + " "
     * + rset.getString("first_name") + " "
     * + rset.getString("last_name") + " "
     * + rset.getInt("salary")
     * );
     * }
     * }
     * catch (Exception e)
     * {
     * System.out.println(e.getMessage());
     * System.out.println("Failed to get salaries by role");
     * }
     * }
     * <p>
     * /**
     * Display an employee.
     */
    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n"
            );
        }
    }

    public static void main(String[] args) {
        App a = new App();

        a.connect();

        ArrayList<Employee> employees = a.getAllSalaries();

        System.out.println("Number of employees: " + employees.size());

        a.printSalaries(employees);

        a.disconnect();
    }
}
