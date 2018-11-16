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
import java.util.List;


/**
 * SQL {@code SELECT} query builder.<br>
 * This class should be instantiated through {@link SQL} static method.
 *
 * @since 0.1
 *
 * @author Vincent Lachenal
 */
public class SelectBuilder {

  // Attributes +
  /** SQL request string buffer */
  final StringBuilder buffer;

  /** Clauses values */
  final List<Object> values;
  // Attributes -


  // Constructors +
  /**
   * {@link SelectBuilder} constructor
   */
  public SelectBuilder() {
    buffer = new StringBuilder("SELECT ");
    values = new ArrayList<>();
  }
  // Constructors -


  // Methods +
  /**
   * Add {@code DISTINCT} command
   *
   * @return {@code this}
   */
  public SelectBuilder distinct() {
    buffer.append("DISTINCT ");
    return this;
  }

  /**
   * Get SQL query.<br>
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return buffer.toString();
  }

  /**
   * Return a new field builder
   *
   * @param column the selected column
   *
   * @return the new {@link FieldsBuilder}
   */
  public FieldsBuilder field(final String column) {
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL {@code GROUP BY} command
   *
   * @param column the first column to group by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder groupBy(final String column) {
    buffer.append(" GROUP BY ");
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL {@code ORDER BY} command.
   *
   * @param column the first column to order by
   *
   * @return a new {@link FieldsBuilder} to add other columns
   */
  public FieldsBuilder orderBy(final String column) {
    buffer.append(" ORDER BY ");
    return new FieldsBuilder(this, column);
  }

  /**
   * Add SQL {@code HAVING} command and its clauses if not empty
   *
   * @param clauses the clauses
   *
   * @return {@code this}
   */
  public SelectBuilder having(final ClausesBuilder clauses) {
    if(!clauses.firstClause) {
      buffer.append(" HAVING ").append(clauses.buffer);
      values.addAll(clauses.values);
    }
    return this;
  }

  /**
   * Add {@code UNION} to other SQL request
   *
   * @param other the other SQL request
   *
   * @return {@code this}
   */
  public SelectBuilder union(final SelectBuilder other) {
    buffer.append(" UNION ").append(other.buffer);
    return this;
  }

  /**
   * Add {@code UNION ALL} to other SQL request with duplicate entries
   *
   * @param other the other SQL request
   *
   * @return {@code this}
   */
  public SelectBuilder unionAll(final SelectBuilder other) {
    buffer.append(" UNION ALL ").append(other.buffer);
    return this;
  }

  /**
   * Add {@code OFFSET n ROWS}
   *
   * @param offset the offset value
   *
   * @return {@code this}
   */
  public SelectBuilder offset(final long offset) {
    buffer.append(" OFFSET ").append(offset).append(" ROWS");
    return this;
  }

  /**
   * Add {@code FETCH FIRST n ROWS ONLY}
   *
   * @param limit the limit value
   *
   * @return {@code this}
   */
  public SelectBuilder fetch(final long limit) {
    buffer.append(" FETCH FIRST ").append(limit).append(" ROWS ONLY");
    return this;
  }

  /**
   * Build SQL query
   *
   * @return the query and its prepared statement values
   */
  public SQLQuery build() {
    return new SQLQuery(buffer.toString(), values);
  }
  // Methods -

}
