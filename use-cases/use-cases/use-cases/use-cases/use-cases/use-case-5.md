# Use Case 5 - Add a New Employee

## Goal in Context

As an HR advisor I want to add a new employee's details so that I can ensure the new employee is paid.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.
- The new employee's details are available.

## Success Condition

The new employee's details are added to the HR system.

## Failed Condition

The employee's details cannot be added and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests to add a new employee.

## Main Success Scenario

1. The HR advisor requests to add a new employee.
2. The system asks for the employee's details.
3. The HR advisor enters the employee's details.
4. The system validates the employee's details.
5. The system adds the employee's details to the database.
6. The system confirms that the employee has been added.

## Extensions

- 3a. The employee details are incomplete.
    - The system informs the HR advisor which details are missing.
- 4a. The employee details are invalid.
    - The system informs the HR advisor that the details are invalid.
- 5a. The database cannot be accessed.
    - The system reports that the employee could not be added.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.