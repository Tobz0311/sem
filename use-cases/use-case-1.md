# Use Case 1 - Produce Salary Report for All Employees

## Goal in Context

As an HR advisor I want to produce a report on the salary of all employees so that I can support financial reporting of the organisation.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.

## Success Condition

A report containing the salary information for all employees is produced.

## Failed Condition

The salary report cannot be produced and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests a report containing the salary of all employees.

## Main Success Scenario

1. The HR advisor requests a salary report for all employees.
2. The system connects to the employee database.
3. The system retrieves the salary information for all employees.
4. The system generates the salary report.
5. The system displays the report to the HR advisor.

## Extensions

- 2a. The database cannot be accessed.
    - The system reports that the database could not be accessed.
- 3a. No employee salary information can be retrieved.
    - The system reports that the salary information could not be retrieved.
- 4a. The report cannot be generated.
    - The system reports that the report could not be generated.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.