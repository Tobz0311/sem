
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class App
{
    Connection con = null;

    public void connect(String location, int delay)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
                System.out.println("Disconnected from database");
            }
            catch (SQLException e)
            {
                System.out.println("Error closing connection to database");
                System.out.println(e.getMessage());
            }
        }
    }

    public void addEmployee(Employee emp)
    {
        try
        {
            Statement stmt = con.createStatement();
            String strUpdate =
                    "INSERT INTO employees (emp_no, first_name, last_name, birth_date, gender, hire_date) " +
                            "VALUES (" + emp.emp_no + ", '" + emp.first_name + "', '" + emp.last_name + "', " +
                            "'9999-01-01', 'M', '9999-01-01')";
            stmt.execute(strUpdate);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to add employee");
        }
    }

    public Employee getEmployee(int ID)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT e.emp_no, e.first_name, e.last_name, " +
                            "t.title, s.salary, d.dept_no, d.dept_name, " +
                            "dm.emp_no AS manager_no " +
                            "FROM employees e " +
                            "LEFT JOIN titles t ON e.emp_no = t.emp_no " +
                            "AND t.to_date = '9999-01-01' " +
                            "LEFT JOIN salaries s ON e.emp_no = s.emp_no " +
                            "AND s.to_date = '9999-01-01' " +
                            "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "AND de.to_date = '9999-01-01' " +
                            "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                            "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "AND dm.to_date = '9999-01-01' " +
                            "WHERE e.emp_no = ?"
            );

            ps.setInt(1, ID);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Employee emp = new Employee();

                emp.emp_no = rs.getInt("emp_no");
                emp.first_name = rs.getString("first_name");
                emp.last_name = rs.getString("last_name");
                emp.title = rs.getString("title");
                emp.salary = rs.getInt("salary");

                String deptNo = rs.getString("dept_no");

                if (deptNo != null)
                {
                    emp.dept = new Department();
                    emp.dept.dept_no = deptNo;
                    emp.dept.dept_name = rs.getString("dept_name");
                }

                int managerNo = rs.getInt("manager_no");

                rs.close();
                ps.close();

                if (!rsWasNullManager(managerNo, emp.emp_no))
                {
                    emp.manager = getEmployeeManager(managerNo);
                }

                return emp;
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private boolean rsWasNullManager(int managerNo, int employeeNo)
    {
        return managerNo == 0 || managerNo == employeeNo;
    }

    private Employee getEmployeeManager(int managerNo)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT emp_no, first_name, last_name " +
                            "FROM employees WHERE emp_no = ?"
            );

            ps.setInt(1, managerNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Employee manager = new Employee();
                manager.emp_no = rs.getInt("emp_no");
                manager.first_name = rs.getString("first_name");
                manager.last_name = rs.getString("last_name");

                rs.close();
                ps.close();

                return manager;
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Employee getEmployee(String firstName, String lastName)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT emp_no FROM employees " +
                            "WHERE first_name = ? AND last_name = ?"
            );

            ps.setString(1, firstName);
            ps.setString(2, lastName);

            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                int employeeID = rs.getInt("emp_no");

                rs.close();
                ps.close();

                return getEmployee(employeeID);
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Department getDepartment(String dept_name)
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT dept_no, dept_name FROM departments " +
                            "WHERE dept_name = ?"
            );

            ps.setString(1, dept_name);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                Department dept = new Department();
                dept.dept_no = rs.getString("dept_no");
                dept.dept_name = rs.getString("dept_name");

                rs.close();
                ps.close();

                PreparedStatement managerPs = con.prepareStatement(
                        "SELECT emp_no FROM dept_manager " +
                                "WHERE dept_no = ? AND to_date = '9999-01-01'"
                );

                managerPs.setString(1, dept.dept_no);
                ResultSet managerRs = managerPs.executeQuery();

                if (managerRs.next())
                {
                    dept.manager = getEmployeeManager(
                            managerRs.getInt("emp_no")
                    );
                }

                managerRs.close();
                managerPs.close();

                return dept;
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Employee> getSalariesByDepartment(Department dept)
    {
        if (dept == null)
        {
            System.out.println("Department not found");
            return null;
        }

        ArrayList<Employee> employees = new ArrayList<Employee>();

        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "WHERE de.dept_no = ? " +
                            "AND de.to_date = '9999-01-01' " +
                            "AND s.to_date = '9999-01-01' " +
                            "ORDER BY e.emp_no"
            );

            ps.setString(1, dept.dept_no);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rs.getInt("emp_no");
                emp.first_name = rs.getString("first_name");
                emp.last_name = rs.getString("last_name");
                emp.salary = rs.getInt("salary");
                employees.add(emp);
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return employees;
    }

    public ArrayList<Employee> getSalariesByRole(String role)
    {
        ArrayList<Employee> employees = new ArrayList<Employee>();

        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e " +
                            "JOIN titles t ON e.emp_no = t.emp_no " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "WHERE t.title = ? " +
                            "AND t.to_date = '9999-01-01' " +
                            "AND s.to_date = '9999-01-01' " +
                            "ORDER BY e.emp_no"
            );

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rs.getInt("emp_no");
                emp.first_name = rs.getString("first_name");
                emp.last_name = rs.getString("last_name");
                emp.salary = rs.getInt("salary");
                emp.title = role;

                employees.add(emp);
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return employees;
    }

    public void outputEmployees(ArrayList<Employee> employees, String filename)
    {
        if (employees == null)
        {
            System.out.println("No employees to write");
            return;
        }

        StringBuilder report = new StringBuilder();

        report.append("# Manager Salaries Report\n\n");
        report.append("| Employee Number | First Name | Last Name | Salary |\n");
        report.append("|---|---|---|---:|\n");

        for (Employee emp : employees)
        {
            report.append("| ")
                    .append(emp.emp_no).append(" | ")
                    .append(emp.first_name).append(" | ")
                    .append(emp.last_name).append(" | ")
                    .append(emp.salary).append(" |\n");
        }

        try
        {
            Files.createDirectories(Path.of("reports"));
            Files.writeString(Path.of("reports", filename), report.toString());
            System.out.println("Report created: reports/" + filename);
        }
        catch (IOException e)
        {
            System.out.println("Failed to create report");
            System.out.println(e.getMessage());
        }
    }

    public void displayEmployee(Employee emp)
    {
        if (emp == null)
        {
            System.out.println("No employee to display");
            return;
        }

        System.out.println("Employee Number: " + emp.emp_no);
        System.out.println("First Name: " + emp.first_name);
        System.out.println("Last Name: " + emp.last_name);
        System.out.println("Title: " + emp.title);
        System.out.println("Salary: " + emp.salary);
    }

    public void printSalaries(ArrayList<Employee> employees)
    {
        if (employees == null)
        {
            System.out.println("No employees to print");
            return;
        }

        for (Employee emp : employees)
        {
            System.out.println(
                    emp.emp_no + " " +
                            emp.first_name + " " +
                            emp.last_name + " " +
                            emp.salary
            );
        }
    }

    public static void main(String[] args)
    {
        App a = new App();

        if (args.length < 1)
        {
            a.connect("localhost:33601", 30000);
        }
        else
        {
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        ArrayList<Employee> employees = a.getSalariesByRole("Manager");

        a.outputEmployees(employees, "ManagerSalaries.md");

        a.disconnect();
    }
}
