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
 * Abstract SQL query part builder.<br>
 * Acts like a decorator to call some initial {@link SelectBuilder} methods.
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public class AbstractPartBuilder {

  // Attributes +
  /** SQL select builder */
  protected final SelectBuilder select;
  // Attributes -


  // Constructors +
  /**
   * {@link AbstractPartBuilder} constructor
   *
   * @param select the select query builder
   */
  protected AbstractPartBuilder(final SelectBuilder select) {
    this.select = select;
  }
  // Constructors -


  // Methods +
  /**
   * Return the original {@link SelectBuilder}
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder done() {
    return select;
  }

  /**
   * Build SQL query
   *
   * @return the query and its prepared statement values
   */
  public SQLQuery build() {
    return select.build();
  }

  /**
   * Add SQL 'group by' command
   *
   * @param column the first column to group by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder groupBy(final String column) {
    return select.groupBy(column);
  }

  /**
   * Add SQL HAVING command and its clauses if not empty
   *
   * @param clauses the clauses
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder having(final ClausesBuilder clauses) {
    return select.having(clauses);
  }

  /**
   * Add SQL 'order by' command.
   *
   * @param column the first column to order by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder orderBy(final String column) {
    return select.orderBy(column);
  }

  /**
   * Add union to other SQL request
   *
   * @param other the other SQL request
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder union(final SelectBuilder other) {
    return select.union(other);
  }

  /**
   * Add union to other SQL request returning duplicate entries
   *
   * @param other the other SQL request
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder unionAll(final SelectBuilder other) {
    return select.unionAll(other);
  }

  /**
   * Add {@code OFFSET n ROWS}
   *
   * @param offset the offset value
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder offset(final long offset) {
    return select.offset(offset);
  }

  /**
   * Add {@code FETCH FIRST n ROWS ONLY}
   *
   * @param limit the limit value
   *
   * @return the {@link SelectBuilder}
   */
  public SelectBuilder fetch(final long limit) {
    return select.fetch(limit);
  }
  // Methods -

}
