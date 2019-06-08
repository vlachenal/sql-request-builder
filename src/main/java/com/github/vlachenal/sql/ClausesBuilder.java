/*
  SQL request builder:  Dynamic SQL request builder for java
  Copyright (C) 2018 Vincent Lachenal

  This file is part of SQL request builder.

  SQL request builder is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  SQL request builder is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with SQL request builder. If not, see <http://www.gnu.org/licenses/>.
*/
package com.github.vlachenal.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * SQL clauses builder.<br>
 * This class should be instantiated through {@link SQL}.
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public class ClausesBuilder {

  // Attributes +
  /** SQL buffer */
  final StringBuilder buffer;

  /** Prepared statement values */
  final List<Object> values;

  /** First clause */
  boolean firstClause = true;
  // Attributes -


  // Constructors +
  /**
   * {@link ClausesBuilder} default constructor
   */
  public ClausesBuilder() {
    buffer = new StringBuilder();
    values = new ArrayList<>();
  }

  /**
   * {@link ClausesBuilder} constructor.<br>
   * This constructor will try to add a first clause if value is valid. Value will
   * be validated with {@code SQL::isValidValue} function.<br>
   * This constructor can be used to add 'EXISTS' clause.
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   */
  public <T> ClausesBuilder(final ClauseMaker clause, final T value) {
    this();
    checkAndAddClause(null, null, clause, value, SQL::isValidValue);
  }

  /**
   * {@link ClausesBuilder} constructor<br>
   * This constructor will try to add a first clause if value is valid.<br>
   * This constructor can be used to add 'EXISTS' clause.
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   */
  public <T> ClausesBuilder(final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    this();
    checkAndAddClause(null, null, clause, value, checker);
  }

  /**
   * {@link ClausesBuilder} constructor.<br>
   * This constructor will try to add a first clause if value is valid. Value will
   * be validated with {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   */
  public <T> ClausesBuilder(final String column, final ClauseMaker clause, final T value) {
    this();
    checkAndAddClause(null, column, clause, value, SQL::isValidValue);
  }

  /**
   * {@link ClausesBuilder} constructor<br>
   * This constructor will try to add a first clause if value is valid.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   */
  public <T> ClausesBuilder(final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    this();
    checkAndAddClause(null, column, clause, value, checker);
  }

  /**
   * {@link ClausesBuilder} constructor.<br>
   * This constructor will try to add a first clause if value is valid. Value will
   * be validated with {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   */
  public <T> ClausesBuilder(final String column, final ClauseMaker clause, final T value1, final T value2) {
    this();
    checkAndAddClause(null, column, clause, value1, value2, SQL::isValidValue);
  }

  /**
   * {@link ClausesBuilder} constructor<br>
   * This constructor will try to add a first clause if value is valid.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   * @param checker the value checker to use
   */
  public <T> ClausesBuilder(final String column, final ClauseMaker clause, final T value1, final T value2, final ValueChecker<T> checker) {
    this();
    checkAndAddClause(null, column, clause, value1, value2, checker);
  }
  // Constructors -


  // Methods +
  // No check no prepared statement values +
  /**
   * Add field to clause
   *
   * @param column the column
   *
   * @return {@code this}
   */
  public ClausesBuilder field(final String column) {
    buffer.append(column);
    firstClause = false;
    return this;
  }

  /**
   * Add {@code NOT} command
   *
   * @param clauses the clauses to negate
   *
   * @return {@code this}
   */
  public ClausesBuilder not(final ClausesBuilder clauses) {
    buffer.append("NOT (").append(clauses.buffer).append(')');
    values.addAll(clauses.values);
    return this;
  }

  /**
   * Add {@code IS NULL} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder isNull() {
    buffer.append(" IS NULL");
    return this;
  }

  /**
   * Add {@code IS NOT NULL} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder isNotNull() {
    buffer.append(" IS NOT NULL");
    return this;
  }

  /**
   * Add not equals clause with {@code <>} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder notEquals() {
    buffer.append(" <> ");
    return this;
  }

  /**
   * Add equals clause with {@code =} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder equals() {
    buffer.append(" = ");
    return this;
  }

  /**
   * Add {@code NOT EXISTS} in subquery clause
   *
   * @param query the select query
   *
   * @return {@code this}
   */
  public ClausesBuilder notExists(final SelectBuilder query) {
    buffer.append("NOT EXISTS(").append(query).append(')');
    values.addAll(query.values);
    return this;
  }

  /**
   * Add {@code EXISTS} in subquery clause
   *
   * @param query the select query
   *
   * @return {@code this}
   */
  public ClausesBuilder exists(final SelectBuilder query) {
    buffer.append("EXISTS(").append(query).append(')');
    values.addAll(query.values);
    return this;
  }

  /**
   * Add {@code IN} clause.<br>
   * Due to many database engine limitation about the maximum number of prepared
   * statement per connection, {@code IN} operator will never be treated with place holder
   * prepared statement values.<br>
   * You can format text values with {@link SQL} utility methods.
   *
   * @param <T> the values' type
   * @param values the values
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder in(final Collection<T> values) {
    buffer.append(" IN ").append(SQL.toSQLList(values));
    return this;
  }

  /**
   * Add {@code NOT IN} clause.<br>
   * Due to many database engine limitation about the maximum number of prepared
   * statement per connection, {@code NOT IN} operator will never be treated with place holder
   * prepared statement values.<br>
   * You can format text values with {@link SQL} utility methods.
   *
   * @param <T> the values' type
   * @param values the values
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder notIn(final Collection<T> values) {
    buffer.append(" NOT IN ").append(SQL.toSQLList(values));
    return this;
  }

  /**
   * Add {@code IN} subquery clause.
   *
   * @param query the subquery
   *
   * @return {@code this}
   */
  public ClausesBuilder in(final SQLQuery query) {
    buffer.append(" IN (").append(query.getQuery()).append(')');
    return this;
  }

  /**
   * Add {@code NOT IN} subquery clause.
   *
   * @param query the subquery
   *
   * @return {@code this}
   */
  public ClausesBuilder notIn(final SQLQuery query) {
    buffer.append(" NOT IN (").append(query.getQuery()).append(')');
    return this;
  }

  /**
   * Add {@code IN} subquery clause.
   *
   * @param query the subquery
   *
   * @return {@code this}
   */
  public ClausesBuilder in(final SelectBuilder query) {
    buffer.append(" IN (").append(query).append(')');
    return this;
  }

  /**
   * Add {@code NOT IN} subquery clause.
   *
   * @param query the subquery
   *
   * @return {@code this}
   */
  public ClausesBuilder notIn(final SelectBuilder query) {
    buffer.append(" NOT IN (").append(query).append(')');
    return this;
  }

  /**
   * Add greater clause with {@code >} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder greater() {
    buffer.append(" > ");
    return this;
  }

  /**
   * Add greater or equals clause with {@code >=} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder greaterEquals() {
    buffer.append(" >= ");
    return this;
  }

  /**
   * Add lesser clause with {@code <} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder lesser() {
    buffer.append(" < ");
    return this;
  }

  /**
   * Add lesser or equals clause with {@code <=} operator
   *
   * @return {@code this}
   */
  public ClausesBuilder lesserEquals() {
    buffer.append(" <= ");
    return this;
  }

  /**
   * Add {@code LIKE} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder like() {
    buffer.append(" LIKE ");
    return this;
  }

  /**
   * Add {@code NOT LIKE} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder notLike() {
    buffer.append(" NOT LIKE ");
    return this;
  }

  /**
   * Add {@code BETWEEN} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder between() {
    buffer.append(" BETWEEN ");
    return this;
  }

  /**
   * Add {@code NOT BETWEEN} clause
   *
   * @return {@code this}
   */
  public ClausesBuilder notBetween() {
    buffer.append(" NOT BETWEEN ");
    return this;
  }

  /**
   * Add {@code AND} to SQL query
   *
   * @return {@code this}
   */
  public ClausesBuilder and() {
    buffer.append(" AND ");
    return this;
  }

  /**
   * Add {@code OR} to SQL query
   *
   * @return {@code this}
   */
  public ClausesBuilder or() {
    buffer.append(" OR ");
    return this;
  }
  // No check no prepared statement values -

  // Check and add value to prepared statement +
  /**
   * Add {@code AND} other clauses if clauses are not {@code null} or empty.
   *
   * @param clauses the clauses to add
   *
   * @return {@code this}
   */
  public ClausesBuilder and(final ClausesProvider clauses) {
    return checkAndAddClauses("AND", clauses);
  }

  /**
   * Add {@code AND} clause if value is valid. Value will be validate with
   * {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final ClauseMaker clause, final T value) {
    return checkAndAddClause("AND", null, clause, value, SQL::isValidValue);
  }

  /**
   * Add {@code AND} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return checkAndAddClause("AND", null, clause, value, checker);
  }

  /**
   * Add {@code AND} clause if value is valid. Value will be validate with
   * {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final String column, final ClauseMaker clause, final T value) {
    return checkAndAddClause("AND", column, clause, value, SQL::isValidValue);
  }

  /**
   * Add {@code AND} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return checkAndAddClause("AND", column, clause, value, checker);
  }

  /**
   * Add {@code AND} clause if value is valid. Value will be validate with
   *  {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final String column, final ClauseMaker clause, final T value1, final T value2) {
    return checkAndAddClause("AND", column, clause, value1, value2, SQL::isValidValue);
  }

  /**
   * Add {@code AND} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder and(final String column, final ClauseMaker clause, final T value1, final T value2, final ValueChecker<T> checker) {
    return checkAndAddClause("AND", column, clause, value1, value2, checker);
  }

  /**
   * Add {@code OR} clause if value is valid. Value will be validate with
   * {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final ClauseMaker clause, final T value) {
    return checkAndAddClause("OR", null, clause, value, SQL::isValidValue);
  }

  /**
   * Add {@code OR} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return checkAndAddClause("OR", null, clause, value, checker);
  }

  /**
   * Add {@code OR} clause if value is valid. Value will be validate with
   * {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final String column, final ClauseMaker clause, final T value) {
    return checkAndAddClause("OR", column, clause, value, SQL::isValidValue);
  }

  /**
   * Add {@code OR} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return checkAndAddClause("OR", column, clause, value, checker);
  }

  /**
   * Add {@code OR} clause if value is valid. Value will be validate with
   * {@code SQL::isValidValue} function.
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final String column, final ClauseMaker clause, final T value1, final T value2) {
    return checkAndAddClause("OR", column, clause, value1, value2, SQL::isValidValue);
  }

  /**
   * Add {@code OR} clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   * @param checker the value checker to use
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder or(final String column, final ClauseMaker clause, final T value1, final T value2, final ValueChecker<T> checker) {
    return checkAndAddClause("OR", column, clause, value1, value2, checker);
  }
  /**
   * Add {@code AND} other clauses if clauses are not {@code null} or empty.
   *
   * @param clauses the clauses to add
   *
   * @return {@code this}
   */
  public ClausesBuilder or(final ClausesProvider clauses) {
    return checkAndAddClauses("OR", clauses);
  }

  /**
   * Add boolean clauses aggregator
   *
   * @param boolAgg the aggregator to use
   */
  private void addBooleanAggregator(final String boolAgg) {
    if(!firstClause) {
      buffer.append(' ').append(boolAgg).append(' ');
    }
  }

  /**
   * Check and add clauses
   *
   * @param boolAgg the boolean aggregator to use
   * @param clauses the clauses to add
   *
   * @return {@code this}
   */
  private ClausesBuilder checkAndAddClauses(final String boolAgg, final ClausesProvider clauses) {
    final ClausesBuilder builder = clauses.getClauses();
    if(builder != null && builder.buffer.length() != 0) {
    	addBooleanAggregator(boolAgg);
      buffer.append(builder.buffer);
      values.addAll(builder.values);
      firstClause = false;
    }
    return this;
  }

  /**
   * Check and add clause
   *
   * @param <T> the value type
   *
   * @param boolAgg the boolean aggregator to use
   * @param column the column (first operand)
   * @param clause the clause type (operator)
   * @param value the value (second operand)
   * @param checker the value checker
   *
   * @return {@code this}
   */
  private final <T> ClausesBuilder checkAndAddClause(final String boolAgg, final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    if(checker.isValid(value)) {
    	addBooleanAggregator(boolAgg);
      buffer.append(clause.makeClause(column));
      if(value instanceof Optional<?>) {
        // Optional value has already been check at his point
      	values.add(((Optional<?>)value).get());
      } else if(value instanceof Collection) { // For (NOT) IN operators
        buffer.append(SQL.toSQLList((Collection<?>)value));
      } else if(value instanceof SelectBuilder) { // For (NOT) EXISTS operators
        buffer.append('(').append(value).append(')');
        values.addAll(((SelectBuilder)value).values);
      } else if(value instanceof SQLQuery) { // For (NOT) EXISTS operators
        final SQLQuery query = (SQLQuery)value;
        buffer.append('(').append(query.getQuery()).append(')');
        values.addAll(query.getValues());
      } else {
      	values.add(value);
      }
      firstClause = false;
    }
    return this;
  }

  /**
   * Check and add clause
   *
   * @param <T> the value type
   *
   * @param boolAgg the boolean aggregator to use
   * @param column the column (first operand)
   * @param clause the clause type (operator)
   * @param value1 the first value (second operand)
   * @param value2 the first value (third operand)
   * @param checker the value checker
   *
   * @return {@code this}
   */
  private final <T> ClausesBuilder checkAndAddClause(final String boolAgg, final String column, final ClauseMaker clause, final T value1, final T value2, final ValueChecker<T> checker) {
    if(checker.isValid(value1) && checker.isValid(value2)) {
    	addBooleanAggregator(boolAgg);
      buffer.append(clause.makeClause(column));
      if(value1 instanceof Optional<?>) {
        // Optional value has already been check at his point
      	values.add(((Optional<?>)value1).get());
      } else {
      	values.add(value1);
      }
      if(value2 instanceof Optional<?>) {
        // Optional value has already been check at his point
      	values.add(((Optional<?>)value2).get());
      } else {
      	values.add(value2);
      }
      firstClause = false;
    }
    return this;
  }
  // Check and add value to prepared statement -

  // Parentheses +
  /**
   * Check and add clauses
   *
   * @param boolAgg the boolean aggregator to use
   * @param other the other clauses to add
   *
   * @return {@code this}
   */
  private ClausesBuilder checkAndAddClauses(final String boolAgg, final ClausesBuilder other) {
    if(!other.firstClause) {
    	addBooleanAggregator(boolAgg);
      buffer.append('(').append(other.buffer).append(')');
      values.addAll(other.values);
      firstClause = false;
    }
    return this;
  }

  /**
   * Add clauses into parenthesis with AND
   *
   * @param other the clauses to add
   *
   * @return {@code this}
   */
  public ClausesBuilder and(final ClausesBuilder other) {
    return checkAndAddClauses("AND", other);
  }

  /**
   * Add clauses into parenthesis with OR
   *
   * @param other the clauses to add
   *
   * @return {@code this}
   */
  public ClausesBuilder or(final ClausesBuilder other) {
    return checkAndAddClauses("OR", other);
  }
  // Parentheses -
  // Methods -

}
