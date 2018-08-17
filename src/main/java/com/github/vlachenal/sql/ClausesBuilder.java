/*
  SQL request builder:  Dynamic SQL request builder for java
  Copyright (C) 2012 Vincent Lachenal

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
import java.util.List;


/**
 * Clauses builder
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
   * {@link ClausesBuilder} constructor
   */
  public ClausesBuilder() {
    buffer = new StringBuilder();
    values = new ArrayList<>();
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
   * Add NOT instruction
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
   * IS NULL clause
   *
   * @return {@code this}
   */
  public ClausesBuilder isNull() {
    buffer.append(" IS NULL");
    return this;
  }

  /**
   * IS NOT NULL clause
   *
   * @return {@code this}
   */
  public ClausesBuilder isNotNull() {
    buffer.append(" IS NOT NULL");
    return this;
  }

  /**
   * Not equals clause
   *
   * @return {@code this}
   */
  public ClausesBuilder notEquals() {
    buffer.append(" <> ");
    return this;
  }

  /**
   * Equals clause
   *
   * @return {@code this}
   */
  public ClausesBuilder equals() {
    buffer.append(" = ");
    return this;
  }

  /**
   * EXISTS clause
   *
   * @param query the select query
   *
   * @return {@code this}
   */
  public ClausesBuilder notExists(final SelectBuilder query) {
    buffer.append("NOT EXISTS(").append(query.toString()).append(')');
    values.addAll(query.values);
    return this;
  }

  /**
   * EXISTS clause
   *
   * @param query the select query
   *
   * @return {@code this}
   */
  public ClausesBuilder exists(final SelectBuilder query) {
    buffer.append("EXISTS(").append(query.toString()).append(')');
    values.addAll(query.values);
    return this;
  }

  /**
   * Add IN clause.<br>
   * Due to many database engine limitation about the maximum number of prepared
   * statement per connection, IN operator will never be treated with place holder
   * prepared statement values.<br>
   * You can format text values with {@link SQL} utility methods.
   *
   * @param <T> the values' type
   * @param values the values
   *
   * @return {@code this}
   */
  public <T> ClausesBuilder in(final List<T> values) {
    final StringBuilder buf = new StringBuilder();
    for(final T val : values) {
      buf.append(',').append(val);
    }
    buffer.append(buf.substring(1));
    return this;
  }

  /**
   * Greater clause
   *
   * @return {@code this}
   */
  public ClausesBuilder greater() {
    buffer.append(" > ");
    return this;
  }

  /**
   * Greater or equals clause
   *
   * @return {@code this}
   */
  public ClausesBuilder greateEquals() {
    buffer.append(" >= ");
    return this;
  }

  /**
   * Lesser clause
   *
   * @return {@code this}
   */
  public ClausesBuilder lesser() {
    buffer.append(" < ");
    return this;
  }

  /**
   * Lesser or equals clause
   *
   * @return {@code this}
   */
  public ClausesBuilder lesserEquals() {
    buffer.append(" <= ");
    return this;
  }

  /**
   * Like clause
   *
   * @return {@code this}
   */
  public ClausesBuilder like() {
    buffer.append(" LIKE ");
    return this;
  }

  /**
   * Not like clause
   *
   * @return {@code this}
   */
  public ClausesBuilder notLike() {
    buffer.append(" NOT LIKE ");
    return this;
  }

  /**
   * Between clause
   *
   * @return {@code this}
   */
  public ClausesBuilder between() {
    buffer.append(" BETWEEN ");
    return this;
  }

  /**
   * Not between clause
   *
   * @return {@code this}
   */
  public ClausesBuilder notBetween() {
    buffer.append(" NOT BETWEEN ");
    return this;
  }

  /**
   * Add AND to SQL query
   *
   * @return {@code this}
   */
  public ClausesBuilder and() {
    buffer.append(" AND ");
    return this;
  }

  /**
   * Add OR to SQL query
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
   * Add AND clause if value is valid. Value will be validate with {@code SQL::isValidValue}
   * function.
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
    checkAndAddClause("AND", column, clause, value, SQL::isValidValue);
    return this;
  }

  /**
   * Add AND clause if value is valid
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
    checkAndAddClause("AND", column, clause, value, checker);
    return this;
  }

  /**
   * Add OR clause if value is valid. Value will be validate with {@code SQL::isValidValue}
   * function.
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
    checkAndAddClause("OR", column, clause, value, SQL::isValidValue);
    return this;
  }

  /**
   * Add OR clause if value is valid
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
    checkAndAddClause("OR", column, clause, value, checker);
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
   */
  private final <T> void checkAndAddClause(final String boolAgg, final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    if(checker.isValid(value)) {
      if(!firstClause) {
        buffer.append(' ').append(boolAgg).append(' ');
      }
      buffer.append(clause.makeClause(column));
      values.add(value);
      firstClause = false;
    }
  }
  // Check and add value to prepared statement -

  /**
   * Add clauses into parenthesis
   *
   * @param other the clauses to add
   *
   * @return {@code this}
   */
  public ClausesBuilder compound(final ClausesBuilder other) {
    buffer.append('(').append(other.buffer).append(')');
    values.addAll(other.values);
    return this;
  }
  // Methods -

}
