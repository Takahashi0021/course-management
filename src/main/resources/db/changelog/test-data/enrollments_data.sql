databaseChangeLog:
  - changeSet:
      id: test-data-02-additional-enrollments
      author: student
      context: test
      changes:
        # Student enrolls in additional courses
        - insert:
            tableName: enrollments
            columns:
              - column: {name: student_id, valueNumeric: 6}
              - column: {name: course_id, valueNumeric: 4}
              - column: {name: progress, valueNumeric: 10}
              - column: {name: status, value: 'ACTIVE'}

        - insert:
            tableName: enrollments
            columns:
              - column: {name: student_id, valueNumeric: 5}
              - column: {name: course_id, valueNumeric: 5}
              - column: {name: progress, valueNumeric: 40}
              - column: {name: status, value: 'ACTIVE'}