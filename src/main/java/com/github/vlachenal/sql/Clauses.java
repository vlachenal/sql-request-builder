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

/**
 * Provides clauses function for optional clauses in {@link ClausesBuilder}.<br>
 * Functions can (should) be used as {@link ClauseMaker}. Every function will format
 * SQL clause for {@link java.sql.PreparedStatement} with the {@code ?} wildcard
 * but {@code in} and {@code notIn} clauses.
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public final class Clauses {

  // Constructors +
  /**
   * {@link Clauses} private constructor.<br>
   * This is a utility classes.
   */
  private Clauses() {
    // Nothing to do
  }
  // Constructors -


  // Methods +
  /**
   * Provides 'not equals' clause with {@code <>} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notEquals(final String column) {
    return column + " <> ?";
  }

  /**
   * Provides 'equals' clause with {@code =} operator
   * ... add 'To' suffix because it does not compile with JDK10 (works fine with 1.8)
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String equalsTo(final String column) {
    return column + " = ?";
  }

  /**
   * Provides 'greater' clause with {@code >} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greater(final String column) {
    return column + " > ?";
  }

  /**
   * Provides 'greater or equals' clause with {@code >=} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greaterEquals(final String column) {
    return column + " >= ?";
  }

  /**
   * Provides 'lesser' clause with {@code <} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesser(final String column) {
    return column + " < ?";
  }

  /**
   * Provides 'lesser or equals' clause with {@code <=} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesserEquals(final String column) {
    return column + " <= ?";
  }

  /**
   * Provides 'like' clause with {@code LIKE} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String like(final String column) {
    return column + " LIKE ?";
  }

  /**
   * Provides 'not like' clause with {@code NOT LIKE} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notLike(final String column) {
    return column + " NOT LIKE ?";
  }

  /**
   * Provides 'between' clause with {@code BETWEEN} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String between(final String column) {
    return column + " BETWEEN ? AND ?";
  }

  /**
   * Provides 'not between' clause with {@code NOT BETWEEN} operator
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notBetween(final String column) {
    return column + " NOT BETWEEN ? AND ?";
  }

  /**
   * Provides 'in' clause with {@code IN} operator.<br>
   * This clause is not managed with {@link java.sql.PreparedStatement}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String in(final String column) {
    return column + " IN ";
  }

  /**
   * Provides 'not in' clause with {@code NOT IN} operator.<br>
   * This clause is not managed with {@link java.sql.PreparedStatement}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notIn(final String column) {
    return column + " NOT IN ";
  }

  /**
   * Provides 'exists' clause with {@code EXISTS} operator.
   *
   * @param column the column (not used)
   *
   * @return clause request part
   */
  public static String exists(final String column) {
    return "EXISTS ";
  }

  /**
   * Provides 'not exists' clause with {@code NOT EXISTS} operator.
   *
   * @param column the column (not used)
   *
   * @return clause request part
   */
  public static String notExists(final String column) {
    return "NOT EXISTS ";
  }

  /**
   * Provides 'equals to any of' clause with {@code = any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String equalsAny(final String column) {
    return column + " = any(?)";
  }

  /**
   * Provides 'not equals to any of' clause with {@code <> any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notEqualsAny(final String column) {
    return column + " <> any(?)";
  }

  /**
   * Provides 'lesser than any of' clause with {@code < any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesserAny(final String column) {
    return column + " < any(?)";
  }

  /**
   * Provides 'lesser than or equals to any of' clause with {@code <= any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesserEqualsAny(final String column) {
    return column + " <= any(?)";
  }

  /**
   * Provides 'greater than to any of' clause with {@code > any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greaterAny(final String column) {
    return column + " > any(?)";
  }

  /**
   * Provides 'greater than or equals to any of' clause with {@code >= any()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greaterEqualsAny(final String column) {
    return column + " >= any(?)";
  }

  /**
   * Provides 'equals to all of' clause with {@code = all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String equalsAll(final String column) {
    return column + " = all(?)";
  }

  /**
   * Provides 'not equals to all of' clause with {@code <> all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String notEqualsAll(final String column) {
    return column + " <> all(?)";
  }

  /**
   * Provides 'lesser than all of' clause with {@code < all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesserAll(final String column) {
    return column + " < all(?)";
  }

  /**
   * Provides 'lesser than or equals to all of' clause with {@code <= all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String lesserEqualsAll(final String column) {
    return column + " <= all(?)";
  }

  /**
   * Provides 'greater than to all of' clause with {@code > all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greaterAll(final String column) {
    return column + " > all(?)";
  }

  /**
   * Provides 'greater than or equals to all of' clause with {@code >= all()} operator.<br>
   * Value has to be {@code java.sql.Array}.
   *
   * @param column the column
   *
   * @return clause request part
   */
  public static String greaterEqualsAll(final String column) {
    return column + " >= all(?)";
  }
  // Methods -

}
