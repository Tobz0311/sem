# Use Case 7 - Update Employee Details

## Goal in Context

As an HR advisor I want to update an employee's details so that the employee's information is kept up to date.

## Scope

HR System

## Level

User goal

## Preconditions

- The HR advisor is using the HR system.
- The employee database is available.
- The employee exists in the database.

## Success Condition

The employee's details are updated in the HR system.

## Failed Condition

The employee's details cannot be updated and the HR advisor is informed that the operation failed.

## Primary Actor

HR advisor

## Trigger

The HR advisor requests to update an employee's details.

## Main Success Scenario

1. The HR advisor requests to update an employee's details.
2. The system asks for the employee's identifier.
3. The HR advisor enters the employee's identifier.
4. The system searches the employee database.
5. The system retrieves the employee's existing details.
6. The system asks the HR advisor for the updated details.
7. The HR advisor enters the updated details.
8. The system validates the updated details.
9. The system updates the employee's details in the database.
10. The system confirms that the employee's details have been updated.

## Extensions

- 3a. The employee identifier is invalid.
    - The system informs the HR advisor that the identifier is invalid.
- 5a. The employee cannot be found.
    - The system informs the HR advisor that the employee could not be found.
- 8a. The updated details are invalid.
    - The system informs the HR advisor that the details are invalid.
- 9a. The database cannot be accessed.
    - The system reports that the employee's details could not be updated.

## Sub-variations

None.

## Schedule

To be completed as part of the HR system development.