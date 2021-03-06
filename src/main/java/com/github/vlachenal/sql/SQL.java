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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * SQL request builder entry point and utility methods
 *
 * @since 0.1
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
   * Initialize a new select builder
   *
   * @return the new {@link SelectBuilder}
   */
  public static SelectBuilder select() {
    return new SelectBuilder();
  }

  /**
   * Initialize a new update builder
   *
   * @param table the table to update
   *
   * @return the new {@link UpdateBuilder}
   */
  public static UpdateBuilder update(final String table) {
    return new UpdateBuilder(table);
  }

  /**
   * Initialize a new delete builder
   *
   * @param table the table to delete
   *
   * @return the new {@link DeleteBuilder}
   */
  public static DeleteBuilder delete(final String table) {
    return new DeleteBuilder(table);
  }

  /**
   * Initialize a new clauses builder
   *
   * @return the new {@link ClausesBuilder}
   */
  public static ClausesBuilder clauses() {
    return new ClausesBuilder();
  }

  /**
   * Initialize a new clauses builder from another one
   *
   * @param other the other {@link ClausesBuilder}
   *
   * @return the new {@link ClausesBuilder}
   */
  public static ClausesBuilder clauses(final ClausesBuilder other) {
    return new ClausesBuilder(other);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final ClauseMaker clause, final T value) {
    return new ClausesBuilder(clause, value);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return new ClausesBuilder(clause, value, checker);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value) {
    return new ClausesBuilder(column, clause, value);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value the value
   * @param checker the value checker to use
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value, final ValueChecker<T> checker) {
    return new ClausesBuilder(column, clause, value, checker);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value1, final T value2) {
    return new ClausesBuilder(column, clause, value1, value2);
  }

  /**
   * Initialize a new clauses builder and add the first clause if value is valid
   *
   * @param <T> the value type
   *
   * @param column the column
   * @param clause the clause maker
   * @param value1 the first value
   * @param value2 the second value
   * @param checker the value checker to use
   *
   * @return the new {@link ClausesBuilder}
   */
  public static <T> ClausesBuilder clauses(final String column, final ClauseMaker clause, final T value1, final T value2, final ValueChecker<T> checker) {
    return new ClausesBuilder(column, clause, value1, value2, checker);
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
  public static List<String> formatText(final Collection<?> values) {
    List<String> texts = null;
    if(values != null) {
      texts = values.stream().map(SQL::formatText).collect(Collectors.toList());
    }
    return texts;
  }

  /**
   * Format value collection for SQL query (i.e for IN clause)
   *
   * @param <T> the collection type
   *
   * @param values the values
   *
   * @return the SQL list
   */
  public static <T> String toSQLList(final Collection<T> values) {
    final StringBuilder buffer = new StringBuilder("(");
    final StringBuilder buf = new StringBuilder();
    for(final T val : values) {
      buf.append(',').append(val);
    }
    buffer.append(buf.substring(1)).append(')');
    return buffer.toString();
  }

  /**
   * Check if value is null or empty (for {@link String} and {@link Collection}).<br>
   * This is the value {@link ValueChecker} which is used ot check value validity.
   *
   * @param <T> the value type
   *
   * @param value the value
   *
   * @return {@code true} is value is not null, {@code false} otherwise
   */
  public static <T> boolean isValidValue(final T value) {
    boolean valid = false;
    if(value != null) {
      if(value instanceof Optional<?>) {
        valid = ((Optional<?>)value).isPresent();
      } else if(value instanceof Collection<?>) {
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
