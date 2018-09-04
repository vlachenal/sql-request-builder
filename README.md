[![Build Status](https://travis-ci.org/vlachenal/sql-request-builder.svg?branch=master)](https://travis-ci.org/vlachenal/sql-request-builder) [![Maintainability](https://api.codeclimate.com/v1/badges/50db46caf82379434877/maintainability)](https://codeclimate.com/github/vlachenal/sql-request-builder/maintainability) [![Test Coverage](https://api.codeclimate.com/v1/badges/50db46caf82379434877/test_coverage)](https://codeclimate.com/github/vlachenal/sql-request-builder/test_coverage)

# sql-request-builder
Dynamic SQL request builder aims to provides a fluent API to easily write dynamic SQL request.

## Synopsis
In most web services, there are search features. These features may have several optional filters which will add clauses in SQL request.

Managing every filter makes your source code more complex and less readable. And the more your service have filters, less your code is readable.

This library provides a fluent API to add clauses only when filter has a valid value (by default not null and not empty). I try to make API to use as easy as if you were writing SQL request.

Every valid value will be added as prepared statement value to avoid SQL injections.

I will not recommended to use this library for static SQL request. It will induce overhead compared to static SQL query string.

## Requirements
sql-request-builder is a standalone library. The only requirement is Java 1.8+.

Build and unit tests are automatically executed with Oracle JDK 1.8 and OpenJDK 10.

## Installation
sql-request-builder library is published on Maven Central.

If you are using Maven, you can add the following dependency to your `pom.xml`:
```xml
<dependency>
  <groupId>com.github.vlachenal</groupId>
  <artifactId>sql-request-builder</artifactId>
  <version>0.2</version>
</dependency>
```

If you are using Gradle, you can add the following dependency:
```groovy
compile "com.github.vlachenal:sql-request-builder:0.2"
```

Otherwise, you can clone this repository and build library using `jar` task.

## Documentation

### Javadoc
You can consult Javadoc [here](https://vlachenal.github.io/sql-request-builder/index.html?com/github/vlachenal/sql/package-summary.html).

### Wiki
[Wiki](https://github.com/vlachenal/sql-request-builder/wiki) contains detailled library usage, releases notes and remaining tasks.

### Examples
More examples can be found in unit tests and wiki.

Here is a sample of library usage:

The following request bean:
```java
public class ExampleRequest {
  public UUID id;
  public String firstName;
  public String lastName;
  public String email;
  public String gender;
  public String country;
}
```

is filled with the following values:
```java
final ExampleRequest req = new ExampleRequest();
req.gender = "F";
req.lastName = "%Croft%";
```

The following code will generate an SQL request to retrieve 'heroes' according to the request:
```java
final SQLQuery query = SQL.select().field("*")
    .from("Heroes")
    .where(SQL.clauses("id", Clauses::equalsTo, req.id)
           .and("first_name", Clauses::like, req.firstName)
           .and("last_name", Clauses::like, req.lastName)
           .and("email", Clauses::equalsTo, req.email)
           .and("gender", Clauses::equalsTo, req.gender)
           .and("country", Clauses::equalsTo, req.country)
        ).build();
```

The generated SQL request will be:
```sql
SELECT * FROM Heroes WHERE last_name LIKE ? AND gender = ?
```

With `%Croft%` and `F` as values.

You can retrieve query and values like this:
```java
String strQuery = query.getQuery();
List<Object> valueLst = query.getValues();
Object[] values = query.values();
```
