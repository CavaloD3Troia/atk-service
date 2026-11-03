Feature: Samples
  A sample created over HTTP is stored, published on atk.sample.created.v1,
  and consumed back by the service, which logs the payload.

  Scenario: Creating a sample
    When a sample "first sample" is created
    Then the response status is 201
    And the sample is stored in the database
    And a SampleCreated message is published
    And the service consumes the message and logs its payload

  Scenario: A sample needs a name
    When a sample "" is created
    Then the response status is 400
