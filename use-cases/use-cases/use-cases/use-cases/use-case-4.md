# Use Case 4 - Produce Salary Report for a Given Role

## Goal in Context

As an HR advisor I want to produce a report on the salary of employees of a given role so that I can support financial reporting of the organisation.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.
- The required role exists.

## Success Condition

A report containing the salary information for employees with the selected role is produced.

## Failed Condition

The salary report cannot be produced and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests a salary report for a given role.

## Main Success Scenario

1. The HR advisor requests a salary report for a role.
2. The system asks for the role.
3. The HR advisor selects a role.
4. The system connects to the employee database.
5. The system retrieves the salary information for employees with the selected role.
6. The system generates the salary report.
7. The system displays the report to the HR advisor.

## Extensions

- 3a. The selected role does not exist.
    - The system informs the HR advisor that the role could not be found.
- 4a. The database cannot be accessed.
    - The system reports that the database could not be accessed.
- 5a. No employees with the selected role can be found.
    - The system reports that no salary information could be retrieved.
- 6a. The report cannot be generated.
    - The system reports that the report could not be generated.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.