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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * SQL request builder entry point
 *
 * @author Vincent Lachenal
 */
public final class SQL {

  // Constructors +
  /**
   * {@link SQL} private constructor
   */
  private SQL() {
    // Nothing to do
  }
  // Constructors -


  // Methods +
  /**
   * Get a new select builder
   *
   * @return the new builder
   */
  public static SelectBuilder select() {
    return new SelectBuilder();
  }

  /**
   * Get a new select builder
   *
   * @return the new builder
   */
  public static SelectBuilder selectDistinct() {
    return new SelectBuilder();
  }

  /**
   * Initialize a new clauses builder
   *
   * @return the new builder
   */
  public static ClausesBuilder clauses() {
    return new ClausesBuilder();
  }

  /**
   * Initialize a new clauses builder
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   *
   * @return the new builder
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value) {
    return new ClausesBuilder(column, clause, value);
  }

  /**
   * Initialize a new clauses builder
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return the new builder
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return new ClausesBuilder(column, clause, value, checker);
  }

  /**
   * Format value for text column adding quote before and after value.<br>
   * {@code Object} will be cast into {@code String} with {@code toString()} method.
   *
   * @param value the value
   *
   * @return the formatter value
   */
  public static String formatText(final Object value) {
    String text = null;
    if(value != null) {
      text = "'" + value.toString() + "'";
    }
    return text;
  }

  /**
   * Format values for text column adding quote before and after value.<br>
   * {@code Object} will be cast into {@code String} with {@code toString()} method.
   *
   * @param values the values
   *
   * @return the formatter values
   */
  public static List<String> formatText(final List<Object> values) {
    List<String> texts = null;
    if(values != null) {
      texts = values.stream().map(SQL::formatText).collect(Collectors.toList());
    }
    return texts;
  }

  /**
   * Check if value is null or empty (according to its type)
   *
   * @param value the value
   *
   * @return {@code true} is value is not null, {@code false} otherwise
   */
  public static <T> boolean isValidValue(final T value) {
    boolean valid = false;
    if(value != null) {
      if(value instanceof Collection) {
        valid = !((Collection<?>)value).isEmpty();
      } else if(value instanceof String) {
        valid = !((String)value).isEmpty();
      } else {
        valid = true;
      }
    }
    return valid;
  }
  // Methods -

}
