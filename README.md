[![Build Status](https://travis-ci.org/vlachenal/sql-request-builder.svg?branch=master)](https://travis-ci.org/vlachenal/sql-request-builder)

# sql-request-builder
Dynamic SQL request builder aims to provides a fluent API to easily write dynamic SQL request.

## Synopsis
In most web services, there are search features. These features may have several optional filters which will add clauses in SQL request.

Managing every filter makes your source code more complex and less readable. And the more your service have filters, less your code is readable.

This library provides a fluent API to add clauses only when filter has a valid value (by default not null and not empty). I try to make API to use as easy as if you were writing SQL request.

Every valid value will be added as prepared statement value to avoid SQL injections.

I will not recommended to use this library for static SQL request. It will induce overhead compared to static SQL query string.

## Documentation

### Javadoc
You can consult Javadoc [here](https://vlachenal.github.io/sql-request-builder/index.html?com/github/vlachenal/sql/package-summary.html).

### Wiki
Wiki is under construction (not started yet ...).

### Examples
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

## TODO
By priority order:
 - Manage optional BETWEEN clause with prepared statement
 - Examples
 - Wiki
 - ~~Manage optional join (for the ones which has to be followed by clauses)~~
 - Manage LIMIT/OFFSET
 - More Javadoc
 - DELETE and UPDATE queries
 - Publish library
