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
   * Provides 'beween' clause with {@code BETWEEN} operator
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
  // Methods -

}
