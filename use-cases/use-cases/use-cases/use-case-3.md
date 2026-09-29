# Use Case 3 - Produce Salary Report for Department Manager

## Goal in Context

As a department manager I want to produce a report on the salary of employees in my department so that I can support financial reporting for my department.

## Scope

HR System

## Level

User goal

## Preconditions

- The department manager is using the HR system.
- The employee database is available.
- The department manager is associated with a department.

## Success Condition

A report containing the salary information for employees in the manager's department is produced.

## Failed Condition

The salary report cannot be produced and the department manager is informed that the operation failed.

## Primary Actor

Department manager

## Trigger

The department manager requests a salary report for their department.

## Main Success Scenario

1. The department manager requests a salary report.
2. The system identifies the department belonging to the department manager.
3. The system connects to the employee database.
4. The system retrieves the salary information for employees in the department.
5. The system generates the salary report.
6. The system displays the report to the department manager.

## Extensions

- 2a. The department manager cannot be associated with a department.
    - The system informs the department manager that their department could not be identified.
- 3a. The database cannot be accessed.
    - The system reports that the database could not be accessed.
- 4a. No employee salary information can be retrieved.
    - The system reports that the salary information could not be retrieved.
- 5a. The report cannot be generated.
    - The system reports that the report could not be generated.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.