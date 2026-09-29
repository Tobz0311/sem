# Use Case 8 - Delete Employee Details

## Goal in Context

As an HR advisor I want to delete an employee's details so that the employee's information can be removed from the HR system.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.
- The employee exists in the database.

## Success Condition

The employee's details are deleted from the HR system.

## Failed Condition

The employee's details cannot be deleted and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests to delete an employee's details.

## Main Success Scenario

1. The HR advisor requests to delete an employee's details.
2. The system asks for the employee's identifier.
3. The HR advisor enters the employee's identifier.
4. The system searches the employee database.
5. The system finds the employee.
6. The system deletes the employee's details from the database.
7. The system confirms that the employee's details have been deleted.

## Extensions

- 3a. The employee identifier is invalid.
    - The system informs the HR advisor that the identifier is invalid.
- 5a. The employee cannot be found.
    - The system informs the HR advisor that the employee could not be found.
- 6a. The database cannot be accessed.
    - The system reports that the employee's details could not be deleted.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.
