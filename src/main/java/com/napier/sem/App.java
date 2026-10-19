
package com.napier.sem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App
{
    Connection con = null;

    public void connect()
    {
        try
        {
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33601/employees?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );

            System.out.println("Connected to database");
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
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

    public void getSalariesByDepartment(Department dept)
    {
        if (dept == null)
        {
            System.out.println("Department not found");
            return;
        }

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
                System.out.println(
                        rs.getInt("emp_no") + " " +
                                rs.getString("first_name") + " " +
                                rs.getString("last_name") + " " +
                                rs.getInt("salary")
                );
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void getAllSalaries()
    {
        try
        {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT e.emp_no, e.first_name, e.last_name, s.salary " +
                            "FROM employees e " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "WHERE s.to_date = '9999-01-01' " +
                            "ORDER BY e.emp_no"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                System.out.println(
                        rs.getInt("emp_no") + " " +
                                rs.getString("first_name") + " " +
                                rs.getString("last_name") + " " +
                                rs.getInt("salary")
                );
            }

            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void printSalaries(Department dept)
    {
        getSalariesByDepartment(dept);
    }

    public void displayEmployee(Employee emp)
    {
        if (emp == null)
        {
            System.out.println("Employee not found");
            return;
        }

        System.out.println("Employee ID: " + emp.emp_no);
        System.out.println("Name: " + emp.first_name + " " + emp.last_name);
        System.out.println("Title: " + emp.title);
        System.out.println("Salary: " + emp.salary);

        if (emp.dept != null)
        {
            System.out.println("Department: " + emp.dept.dept_name);
        }

        if (emp.manager != null)
        {
            System.out.println(
                    "Manager: " + emp.manager.first_name + " " +
                            emp.manager.last_name
            );
        }
    }

    public static void main(String[] args)
    {
        App app = new App();
        app.connect();

        if (app.con == null)
        {
            System.out.println("Could not connect to database");
            return;
        }

        Department dept = app.getDepartment("Sales");

        if (dept != null)
        {
            System.out.println("Department: " + dept.dept_name);

            if (dept.manager != null)
            {
                System.out.println(
                        "Manager: " + dept.manager.first_name + " " +
                                dept.manager.last_name
                );
            }

            app.getSalariesByDepartment(dept);
        }

        try
        {
            app.con.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
