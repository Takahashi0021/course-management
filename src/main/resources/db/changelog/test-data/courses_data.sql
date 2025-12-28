databaseChangeLog:
  - changeSet:
      id: test-data-01-additional-courses
      author: student
      context: test
      changes:
        - insert:
            tableName: courses
            columns:
              - column: {name: title, value: 'Artificial Intelligence Fundamentals'}
              - column: {name: description, value: 'Introduction to AI concepts and machine learning'}
              - column: {name: instructor_id, valueNumeric: 2}
              - column: {name: price, valueNumeric: 79.99}
              - column: {name: is_published, valueBoolean: true}

        - insert:
            tableName: courses
            columns:
              - column: {name: title, value: 'Cybersecurity Basics'}
              - column: {name: description, value: 'Fundamentals of cybersecurity and network protection'}
              - column: {name: instructor_id, valueNumeric: 3}
              - column: {name: price, valueNumeric: 39.99}
              - column: {name: is_published, valueBoolean: true}