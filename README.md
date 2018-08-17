[![Build Status](https://travis-ci.org/vlachenal/sql-request-builder.svg?branch=master)](https://travis-ci.org/vlachenal/sql-request-builder)

# sql-request-builder
Dynamic SQL request builder aims to way a fluent API to easily write dynamic SQL request.

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
TODO

## TODO
By priority order:
 - Manage optional BETWEEN clause with prepared statement
 - Examples
 - Wiki
 - Manage optional join (for the ones which has to be followed by clauses)
 - Manage LIMIT/OFFSET
 - More Javadoc
 - DELETE and UPDATE queries
