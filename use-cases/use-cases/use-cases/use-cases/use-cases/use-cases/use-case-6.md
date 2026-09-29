# Use Case 6 - View Employee Details

## Goal in Context

As an HR advisor I want to view an employee's details so that the employee's promotion request can be supported.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.
- The employee exists in the database.

## Success Condition

The employee's details are displayed to the HR advisor.

## Failed Condition

The employee's details cannot be retrieved and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests to view an employee's details.

## Main Success Scenario

1. The HR advisor requests to view an employee's details.
2. The system asks for the employee's identifier.
3. The HR advisor enters the employee's identifier.
4. The system searches the employee database.
5. The system retrieves the employee's details.
6. The system displays the employee's details to the HR advisor.

## Extensions

- 3a. The employee identifier is invalid.
    - The system informs the HR advisor that the identifier is invalid.
- 4a. The database cannot be accessed.
    - The system reports that the database could not be accessed.
- 5a. The employee cannot be found.
    - The system informs the HR advisor that the employee could not be found.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.